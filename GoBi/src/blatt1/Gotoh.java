package blatt1;

import java.util.HashMap;

public class Gotoh {

    private String seqlib;
    private String pairfile;
    private String matrix;
    private double gapopen;
    private double gapextend;
    private String mode;
    private boolean printali;
    private String printmatrices;
    private boolean check;

    public Gotoh(HashMap<String, String> params) {
        seqlib = params.get("-seqlib");
        pairfile = params.get("-pairs");
        matrix = params.containsKey("-m") ? params.get("-m") : "dayhoff";
        gapopen = params.containsKey("-go") ? Double.parseDouble(params.get("-go")) : -12;
        gapextend = params.containsKey("-ge") ? Double.parseDouble(params.get("-ge")) : -1;
        mode = params.containsKey("-mode") ? params.get("-mode") : "freeshift";
        printali = params.containsKey("-printali") ? true : false;
        printmatrices = params.containsKey("-printmatrices") ? "txt" : "";
        check = params.containsKey("-check") ? true : false;
    }

    public void fillMatrix() {
    }

    public void backtracking() {
    }
}
