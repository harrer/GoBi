package blatt1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Gotoh {

    private HashMap<String, String> seqlib;
    private ArrayList<SeqPair> pairfile;
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
        startAlignment();
    }

    private void startAlignment() throws IOException {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        for (SeqPair pair : pairfile) {
            seq1 = seqlib.get(pair.getS1());
            seq2 = seqlib.get(pair.getS2());
            sb.append(">").append(pair.getS1()).append(" ").append(pair.getS2()).append(" ");
            A = new double[seq1.length() + 1][seq2.length() + 1];
            I = new double[seq1.length() + 1][seq2.length() + 1];
            D = new double[seq1.length() + 1][seq2.length() + 1];
            AlignmentMax result = fillMatrix();
            sb.append(df.format(result.getMax())).append("\n");
            if(printali){
                String[] backtrack = {};
                switch(mode){
                    case "global":
                        backtrack = backtrackingGlobal();
                        break;
                    case "local":
                        break;
                    case "freeshift":
                    break;
                }
                sb.append(pair.getS1()).append(": ").append(backtrack[0]).append("\n").append(pair.getS2()).append(": ").append(backtrack[1]).append("\n");
            }
        }
//        FileWriter writer = new FileWriter(new File("/home/tobias/Desktop/out.scores"));
//        writer.write(sb.toString());
//        writer.close();
    }

    private AlignmentMax fillMatrix() {
        for (int i = 1; i < seq1.length() + 1; i++) {//init
            A[i][0] = mode.equals("global") ? g(i) : 0;
            D[i][0] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 1; i < seq2.length() + 1; i++) {
            A[0][i] = mode.equals("global") ? g(i) : 0;
            I[0][i] = Double.NEGATIVE_INFINITY;
        }
        AlignmentMax lMax = new AlignmentMax(0, 0, Double.NEGATIVE_INFINITY, "local");
        AlignmentMax fMax = new AlignmentMax(0, 0, Double.NEGATIVE_INFINITY, "freeshift");
        for (int i = 1; i < seq1.length() + 1; i++) {
            for (int j = 1; j < seq2.length() + 1; j++) {
                I[i][j] = Math.max(A[i - 1][j] + g(1), I[i - 1][j] + gapextend);
                D[i][j] = Math.max(A[i][j - 1] + g(1), D[i][j - 1] + gapextend);
                A[i][j] = mode.equals("local") ? Math.max(0, Math.max(A[i - 1][j - 1] + getCost(i - 1, j - 1), Math.max(D[i][j], I[i][j]))) : Math.max(A[i - 1][j - 1] + getCost(i - 1, j - 1), Math.max(D[i][j], I[i][j]));
                if (A[i][j] >= lMax.getMax()) {
                    lMax.setMax(i, j, A[i][j]);
                }
                if ((i == seq1.length() || j == seq2.length()) && A[i][j] >= fMax.getMax()) {
                    fMax.setMax(i, j, A[i][j]);
                }
            }
        }
        switch (mode) {
            case "global":
                return new AlignmentMax(seq1.length(), seq2.length(), A[seq1.length()][seq2.length()], mode);
            case "local":
                return lMax;
            case "freeshift":
                return fMax;
        }
        return null;
    }

    private String[] backtrackingGlobal() {
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        int i = seq1.length(), j = seq2.length();
        while (i > 0 && j > 0) {
            if (A[i][j] == (A[i - 1][j - 1] + getCost(i - 1, j - 1))) {
                i--;
                j--;
                s1.append(seq1.charAt(i));
                s2.append(seq2.charAt(j));
            } else if (A[i][j] == I[i][j]) {
                int k = 1;
                s1.append(seq1.charAt(i - 1));
                s2.append('-');
                while (!((A[i - k][j] + g(k)) == A[i][j])) {
                    k++;
                    s1.append(seq1.charAt(i - k));
                    s2.append('-');
                }
                i -= k;
            } else if (A[i][j] == D[i][j]) {
                int k = 1;
                s2.append(seq2.charAt(i - 1));
                s1.append('-');
                while (!((A[i][j - k] + g(k)) == A[i][j])) {
                    k++;
                    s2.append(seq2.charAt(i - k));
                    s1.append('-');
                }
                j -= k;
            }
        }
        if (i == 0) {
            while (j > 0) {
                s2.append(seq2.charAt(j - 1));
                s1.append('-');
                j--;
            }
        } else if (j == 0) {
            while (i > 0) {
                s1.append(seq1.charAt(i - 1));
                s2.append('-');
                i--;
            }
        }
        String[] out = {s1.reverse().toString(), s2.reverse().toString()};
        return out;
    }

    private double g(int n) {
        return gapopen + n * gapextend;
    }

    private double getCost(int i, int j) {
        return matrix[aminoAcids.get(seq1.charAt(i))][aminoAcids.get(seq2.charAt(j))];
    }

    private void initParams(HashMap<String, String> params) throws IOException {
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

    public String printMatrix() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        for (int i = 0; i < seq2.length() + 1; i++) {
            for (int j = 0; j < seq1.length() + 1; j++) {
                sb.append(df.format(A[j][i])).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
