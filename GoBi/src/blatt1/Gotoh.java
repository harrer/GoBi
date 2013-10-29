package blatt1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
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

    private String seq1 = "WTHGQA";
    private String seq2 = "WTHA";

    public Gotoh(HashMap<String, String> params) throws IOException {
        long start = new Date().getTime();
        initParams(params);
        StringBuilder sb = null;
        switch (mode) {
            case "global":
                sb = startAlignmentGlobal();
                break;
            case "local":
                sb = startAlignmentLocal();
                break;
            case "freeshift":
                sb = startAlignmentFreeshift();
                break;
        }
        System.out.println("Alignment completed! Writing to file");
        FileWriter writer = null;
        if(printali){
            writer = new FileWriter(new File("/home/h/harrert/Desktop/out.alignments"));
        }
        else{
            writer = new FileWriter(new File("/home/h/harrert/Desktop/out.scores"));
        }
        writer.write(sb.toString());
        writer.close();
        long end = new Date().getTime();
        long time = end - start;
        System.out.println(checkScoreLocal("GVVHDDMECSHYMKNFD--------------VGHVPIRLPRTKHLLNVINENFGTLAFCRRWLDR-LGESKYLMALKNLCDLGIVDPYPPLCD--", "-----------------GQVIEVPPTLIYMYVRDVPVRVAQARFLLAKIKREYGTLPFAYRWLQNDMPEGQLKLALKTLEKAGAIYGYPVL--KE"));
        System.out.println("Done! " + time / 60000 + " min, " + (time / 1000) % 60 + " s.\nTotal: " + time + " ms");
    }

    private StringBuilder startAlignmentFreeshift() throws IOException {
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        StringBuilder sb = new StringBuilder();
        double c = 0.0;
        int f = 1;
        if (!printali) {
            for (SeqPair pair : pairfile) {
                if (c / pairfile.size() >= f * 0.01) {
                    System.out.println(f + "% completed");
                    f++;
                }
                c++;
                seq1 = seqlib.get(pair.getS1());
                seq2 = seqlib.get(pair.getS2());
                sb.append(pair.getS1());sb.append(" ");sb.append(pair.getS2());sb.append(" ");sb.append(df.format(fillMatrixGlobal() / 10.0));sb.append("\n");
            }
        } else {
            for (SeqPair pair : pairfile) {
                if (c / pairfile.size() >= f * 0.01) {
                    System.out.println(f + "% completed");
                    f++;
                }
                c++;
                seq1 = seqlib.get(pair.getS1());
                seq2 = seqlib.get(pair.getS2());
                sb.append(">");sb.append(pair.getS1());sb.append(" ");sb.append(pair.getS2());sb.append(" ");sb.append(df.format(fillMatrixGlobal() / 10.0));sb.append("\n");
                String[] backtrack = {};
                backtrack = backtrackingGlobal();
                sb.append(pair.getS1());sb.append(": ");sb.append(backtrack[0]);sb.append("\n");sb.append(pair.getS2());sb.append(": ");sb.append(backtrack[1]);sb.append("\n");
            }
        }
        return sb;
    }

    private StringBuilder startAlignmentLocal() throws IOException {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        double c = 0.0;
        int f = 1;
        if (!printali) {
            for (SeqPair pair : pairfile) {
                if (c / pairfile.size() >= f * 0.01) {
                    System.out.println(f + "% completed");
                    f++;
                }
                c++;
                seq1 = seqlib.get(pair.getS1());
                seq2 = seqlib.get(pair.getS2());
                sb.append(pair.getS1());sb.append(" ");sb.append(pair.getS2());sb.append(" ");sb.append(df.format(fillMatrixGlobal() / 10.0));sb.append("\n");
            }
        } else {
            for (SeqPair pair : pairfile) {
                if (c / pairfile.size() >= f * 0.01) {
                    System.out.println(f + "% completed");
                    f++;
                }
                c++;
                seq1 = seqlib.get(pair.getS1());
                seq2 = seqlib.get(pair.getS2());
                sb.append(">");sb.append(pair.getS1());sb.append(" ");sb.append(pair.getS2());sb.append(" ");sb.append(df.format(fillMatrixGlobal() / 10.0));sb.append("\n");
                String[] backtrack = {};
                backtrack = backtrackingGlobal();sb.append(pair.getS1());sb.append(": ");sb.append(backtrack[0]);sb.append("\n");sb.append(pair.getS2());sb.append(": ");sb.append(backtrack[1]);sb.append("\n");
            }
        }
        return sb;
    }

    private StringBuilder startAlignmentGlobal() throws IOException {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        double c = 0.0;
        int f = 1;
        if (!printali) {
            for (SeqPair pair : pairfile) {
                if (c / pairfile.size() >= f * 0.01) {
                    System.out.println(f + "% completed");
                    f++;
                }
                c++;
                seq1 = seqlib.get(pair.getS1());
                seq2 = seqlib.get(pair.getS2());
                sb.append(pair.getS1());sb.append(" ");sb.append(pair.getS2());sb.append(" ");sb.append(df.format(fillMatrixGlobal() / 10.0));sb.append("\n");
            }
        } else {
            for (SeqPair pair : pairfile) {
                if (c / pairfile.size() >= f * 0.01) {
                    System.out.println(f + "% completed");
                    f++;
                }
                c++;
                seq1 = seqlib.get(pair.getS1());
                seq2 = seqlib.get(pair.getS2());
                sb.append(">");sb.append(pair.getS1());sb.append(" ");sb.append(pair.getS2());sb.append(" ");sb.append(df.format(fillMatrixGlobal() / 10.0));sb.append("\n");
                String[] backtrack = {};
                backtrack = backtrackingGlobal();
                sb.append(pair.getS1());sb.append(": ");sb.append(backtrack[0]);sb.append("\n");sb.append(pair.getS2());sb.append(": ");sb.append(backtrack[1]);sb.append("\n");
            }
        }
        return sb;
    }

    private AlignmentMax fillMatrixFreeshift() {
        AlignmentMax max = null;
        max = new AlignmentMax(0, 0, Integer.MIN_VALUE, "freeshift");
        for (int i = 1; i < seq1.length() + 1; i++) {
            for (int j = 1; j < seq2.length() + 1; j++) {
                I[i][j] = Math.max(A[i - 1][j] + g(1), I[i - 1][j] + gapextend);
                D[i][j] = Math.max(A[i][j - 1] + g(1), D[i][j - 1] + gapextend);
                int a = Math.max(D[i][j], I[i][j]);
                A[i][j] = Math.max(0, Math.max(A[i - 1][j - 1] + getCost(seq1.charAt(i - 1), seq2.charAt(j - 1)), a));
                if (A[i][j] >= max.getMax()) {
                    max.setMax(i, j, A[i][j]);
                }
            }
        }
        return max;
    }

    private AlignmentMax fillMatrixLocal() {
        AlignmentMax max = null;
        max = new AlignmentMax(0, 0, Integer.MIN_VALUE, "local");
        for (int i = 1; i < seq1.length() + 1; i++) {
            for (int j = 1; j < seq2.length() + 1; j++) {
                I[i][j] = Math.max(A[i - 1][j] + g(1), I[i - 1][j] + gapextend);
                D[i][j] = Math.max(A[i][j - 1] + g(1), D[i][j - 1] + gapextend);
                int a = Math.max(D[i][j], I[i][j]);
                A[i][j] = Math.max(0, Math.max(A[i - 1][j - 1] + getCost(seq1.charAt(i - 1), seq2.charAt(j - 1)), a));
                if (A[i][j] >= max.getMax()) {
                    max.setMax(i, j, A[i][j]);
                }
            }
        }
        return max;
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
        String[] out = {s1.reverse().toString(), s2.reverse().toString()};
        return out;
    }
    
    private double checkScoreGlobal(String s1, String s2){
        int score = 0;
        for (int i = 0; i < s1.length(); i++) {
            if(s1.charAt(i) == '-'){
                int k=1;
                while((i+k)<s1.length() && s1.charAt(i+k) == '-'){
                    k++;
                }
                score += g(k);
                i += (k-1);
            }
            else if(s2.charAt(i) == '-'){
                int k=1;
                while((i+k)<s2.length() && s2.charAt(i+k) == '-'){
                    k++;
                }
                score += g(k);
                i += (k-1);
            }
            else{
                score += getCost(s1.charAt(i), s2.charAt(i));
            }
        }
        return score/10.0;
    }
    
    private double checkScoreLocal(String s1, String s2){
        int end = -1, start = -1, score = 0;
        for (int i = 0; i < s1.length(); i++) {
            if(s1.charAt(i) != '-' && s2.charAt(i) != '-'){
                start = i;
                break;
            }
        }
        for (int i = s1.length()-1; i >= 0; i--) {
            if(s1.charAt(i) != '-' && s2.charAt(i) != '-'){
                end = i;
                break;
            }
        }
        for (int i = start; i <= end; i++) {
            if(s1.charAt(i) != '-' && s2.charAt(i) != '-'){
                score += getCost(s1.charAt(i), s2.charAt(i));
            }
            else{
                int k=1;
                while((i+k)< s1.length() && (s1.charAt(i+k) == '-' || s2.charAt(i+k) == '-')){
                    k++;
                }
                score += g(k);
                i += (k-1);
            }
        }
        return score/10.0;
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
        pairfile = parser.parsePairFile(params.get("-pairs"));
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
        A = new int[size + 1][size + 1];
        I = new int[size + 1][size + 1];
        D = new int[size + 1][size + 1];
        for (int i = 1; i < size + 1; i++) {//init
            A[i][0] = g(i);
            D[i][0] = -99999999;
        }
        for (int i = 1; i < size + 1; i++) {
            A[0][i] = g(i);
            I[0][i] = -99999999;
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
