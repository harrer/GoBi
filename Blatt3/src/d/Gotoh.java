package d;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Gotoh {

    private HashMap<String, String> seqlib;
    private ArrayList<SeqPair> pairfile;
    private int[][] matrix;
    private int gapopen;
    private int gapextend;
    private String mode;
    private boolean printali;
    private String printmatrices;
    private boolean check;
    private HashMap<Character, Integer> aminoAcids;

    private int[][] A;
    private int[][] I;
    private int[][] D;

    private String seq1;
    private String seq2;

    public Gotoh(HashMap<String, String> params, ArrayList<SeqPair> pairs) throws IOException {
        initParams(params);
        pairfile = pairs;
        //HashMap<String, Integer> map = startAlignmentGlobal();
    }

    public HashMap<String, Double> startAlignmentGlobal() throws IOException {
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        HashMap<String, Double> map = new HashMap();
        Double c=0.0, p=0.0;
        for (SeqPair pair : pairfile) {
            c++;
            if(c/pairfile.size() >=p/100){
                System.out.println(p.intValue()+"%");
                p++;
            }
            seq1 = seqlib.get(pair.getS1());
            seq2 = seqlib.get(pair.getS2());
            fillMatrixGlobal();
            String[] backtrack = backtrackingGlobal();
            String query = backtrack[0];
            String target = backtrack[1];
            int matches = 0;
            for (int i = 0; i <query.length(); i++) {
                if(query.charAt(i) == target.charAt(i)){
                    matches++;
                }
            }
            map.put(pair.getS1(), 1.0*matches/query.length());
        }
        return map;
    }

    private int fillMatrixGlobal() {
        for (int i = 1; i < seq1.length() + 1; i++) {
            for (int j = 1; j < seq2.length() + 1; j++) {
                I[i][j] = Math.max(A[i - 1][j] + g(1), I[i - 1][j] + gapextend);
                D[i][j] = Math.max(A[i][j - 1] + g(1), D[i][j - 1] + gapextend);
                int a = Math.max(D[i][j], I[i][j]);
                A[i][j] = Math.max(A[i - 1][j - 1] + getCost(seq1.charAt(i - 1), seq2.charAt(j - 1)), a);
            }
        }
        return A[seq1.length()][seq2.length()];
    }

    private String[] backtrackingGlobal() {
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        int i = seq1.length(), j = seq2.length();
        while (i > 0 && j > 0) {
            if (A[i][j] == (A[i - 1][j - 1] + getCost(seq1.charAt(i - 1), seq2.charAt(j - 1)))) {
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
                s2.append(seq2.charAt(j - 1));
                s1.append('-');
                while (!((A[i][j - k] + g(k)) == A[i][j])) {
                    k++;
                    s2.append(seq2.charAt(j - k));
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
        return new String[]{s1.reverse().toString(), s2.reverse().toString()};
    }

    private int g(int n) {
        return gapopen + n * gapextend;
    }

    private int getCost(char i, char j) {
        return matrix[i - 65][j - 65];
    }

    private void initParams(HashMap<String, String> params) throws IOException {
        Parser parser = new Parser();
        seqlib = parser.parseSeqlib(params.get("-seqlib"));
        //pairfile = parser.parsePairFile(params.get("-pairs"));
        matrix = params.containsKey("-m") ? parser.parseMatrix(params.get("-m"), true) : parser.parseMatrix("dayhoff", true);
        gapopen = params.containsKey("-go") ? (new Double(Double.parseDouble(params.get("-go")) * 10)).intValue() : -120;
        gapextend = params.containsKey("-ge") ? (new Double(Double.parseDouble(params.get("-ge")) * 10)).intValue() : -10;
        mode = params.containsKey("-mode") ? params.get("-mode") : "freeshift";
        printali = params.containsKey("-printali");
        printmatrices = params.containsKey("-printmatrices") ? "txt" : "";
        check = params.containsKey("-check");
        String aa = "ARNDCQEGHILKMFPSTWYV";
        aminoAcids = new HashMap<>();
        for (int i = 0; i < aa.length(); i++) {
            aminoAcids.put(aa.charAt(i), i);
        }
        int size = Integer.parseInt(seqlib.get("_maxLength_"));
        size = (size > 10000) ? 10000 : size;
        A = new int[size + 1][size + 1];
        I = new int[size + 1][size + 1];
        D = new int[size + 1][size + 1];
        for (int i = 1; i < size + 1; i++) {//init
            A[i][0] = (mode.equals("global")) ? g(i) : 0;
            D[i][0] = -99999999;
        }
        for (int i = 1; i < size + 1; i++) {
            A[0][i] = (mode.equals("global")) ? g(i) : 0;
            I[0][i] = -99999999;
        }
    }
}
