package blatt1;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Gotoh {

    private HashMap seqlib;
    private ArrayList pairfile;
    private double[][] matrix;
    private double gapopen;
    private double gapextend;
    private String mode;
    private boolean printali;
    private String printmatrices;
    private boolean check;
    private HashMap<Character, Integer> aminoAcids;
    
    private double[][] A;
    private double[][] I;
    private double[][] D;
    
    private String seq1 = "WTHGQA";
    private String seq2 = "WTHA";

    public Gotoh(HashMap<String, String> params) throws IOException {
        initParams(params);
        A = new double[seq1.length()+1][seq2.length()+1];
        I = new double[seq1.length()+1][seq2.length()+1];
        D = new double[seq1.length()+1][seq2.length()+1];
    }
    
    public void fillMatrix() {
        for (int i = 1; i < seq1.length()+1; i++) {//init
            A[i][0] = g(i);
            D[i][0] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 1; i < seq2.length()+1; i++) {
            A[0][i] = g(i);
            I[0][i] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 1; i < seq1.length()+1; i++) {
            for (int j = 1; j < seq2.length()+1; j++) {
                I[i][j] = Math.max(A[i-1][j] + g(1), I[i-1][j] + gapextend);
                D[i][j] = Math.max(A[i][j-1] + g(1), D[i][j-1] + gapextend);
                A[i][j] = Math.max(A[i-1][j-1] + getCost(i-1, j-1), Math.max(D[i][j], I[i][j]));
            }
        }
    }

    public void backtracking() {
    }
    
    private double g(int n){
        return gapopen+n*gapextend;
    }
    
    private double getCost(int i, int j){
        return matrix[aminoAcids.get(seq1.charAt(i))][aminoAcids.get(seq2.charAt(j))];
    }
    
    private void initParams(HashMap<String, String> params) throws IOException{
        Parser parser = new Parser();
        seqlib = parser.parseSeqlib(params.get("-seqlib"));
        pairfile = parser.parsePairFile(params.get("-pairs"));
        matrix = params.containsKey("-m") ? parser.parseMatrix(params.get("-m"), true) : parser.parseMatrix("dayhoff", true);
        gapopen = params.containsKey("-go") ? Double.parseDouble(params.get("-go")) : -12;
        gapextend = params.containsKey("-ge") ? Double.parseDouble(params.get("-ge")) : -1;
        mode = params.containsKey("-mode") ? params.get("-mode") : "freeshift";
        printali = params.containsKey("-printali");
        printmatrices = params.containsKey("-printmatrices") ? "txt" : "";
        check = params.containsKey("-check");
        String aa = "ARNDCQEGHILKMFPSTWYV";
        aminoAcids = new HashMap<>();
        for (int i = 0; i < aa.length(); i++) {
            aminoAcids.put(aa.charAt(i), i);
        }
    }
    
    public String printMatrix(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < seq2.length()+1; i++) {
            for (int j = 0; j < seq1.length()+1; j++) {
                sb.append(df.format(A[i][j])).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
