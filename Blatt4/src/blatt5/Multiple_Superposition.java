package blatt5;

import blatt4.PDBParser;
import blatt4.Superposition;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Tobias
 */
public class Multiple_Superposition {

    public static String[] tm_parser(BufferedReader br, String file, boolean bufferedReader) throws IOException {
        br = bufferedReader ? br : new BufferedReader(new FileReader(file));
        String line;
        ArrayList<String> list = new ArrayList();
        boolean read = false;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("Chain")) {
                list.add(line.split("\\s+")[3]);
            }
            if (line.startsWith("(\":\"")) {
                read = true;
            }
            if (read) {
                list.add(line);
            }
        }
        return list.toArray(new String[]{});
    }

    public static ArrayList<String[]> read_SimList(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] split;
        ArrayList<String[]> list = new ArrayList();
        while ((line = br.readLine()) != null) {
            split = line.split("\\s+");
            list.add(new String[]{split[1], split[5], split[8], split[10], split[12]});//Q, Q.length, ali.length, tm_score, rmsd
        }
        return list;
    }

    public static DoubleMatrix2D readPDB(String file, ArrayList<Boolean> aligned, boolean full) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        ArrayList<Double[]> coordinates = new ArrayList();
        if (full) {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ATOM")) {
                    String[] split = line.split("\\s+");
                    coordinates.add(new Double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
                }
            }
        } else {
            int c = -1;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (split[2].equalsIgnoreCase("CA")) {
                    c++;
                    if (aligned.get(c).booleanValue()) {
                        coordinates.add(new Double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
                    }
                }
            }
        }
        double[][] d = new double[coordinates.size()][3];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < 3; j++) {
                d[i][j] = coordinates.get(i)[j];
            }
        }
        return new DenseDoubleMatrix2D(d);
    }

    public static ArrayList<Boolean> alignedPositions(String[] ali, boolean upper) {
        ArrayList<Boolean> list = new ArrayList<>();
        String s = upper ? ali[3] : ali[5];
        for (int i = 0; i < ali[3].length(); i++) {
            if (s.charAt(i) != '-') {
                if ((ali[4].charAt(i) == ':') || (ali[4].charAt(i) == '.')) {
                    list.add(Boolean.TRUE);
                } else {
                    list.add(Boolean.FALSE);
                }
            }
        }
        return list;
    }

    public static ArrayList<DoubleMatrix2D> multi_superpose(String template, ArrayList<String[]> targets) throws IOException {
        ArrayList<DoubleMatrix2D> list = new ArrayList();
        String pdbPath = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/STRUCTURES/";
        for (String[] s : targets) {
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cd /home/proj/biosoft/PROTEINS/software/; ./TMalign " + pdbPath + template + ".pdb " + pdbPath + s[0] + ".pdb").getInputStream()));
            String[] ali = tm_parser(br, "", true);
            br.close();
            DoubleMatrix2D P = (DenseDoubleMatrix2D) readPDB(template, alignedPositions(ali, true), false);
            DoubleMatrix2D Q = (DenseDoubleMatrix2D) readPDB(template, alignedPositions(ali, false), false);
            Object[] sp = Superposition.superimpose(P, Q, null, false);
            list.add((DoubleMatrix2D) sp[0]);
        }
        return list;
    }

    public static void combinedPDB(ArrayList<String[]> simList) throws IOException {
        String pdbPath = "/Users/Tobias/Desktop/";//"/home/proj/biosoft/PROTEINS/CATHSCOP/TMALIGN/";
        double max = -1;
        int maxPos = -1;
        for (int i = 0; i < simList.size(); i++) {
            double d = Double.parseDouble(simList.get(i)[3]);
            if (d > max && d < 1.0) {
                max = d;
                maxPos = i;
            }
        }
        double max2 = -1;
        int maxPos2 = -1;
        for (int i = 0; i < simList.size(); i++) {
            double d = Double.parseDouble(simList.get(i)[3]);
            if (d > max2 && i != maxPos && d < 1.0) {
                max2 = d;
                maxPos2 = i;
            }
        }
        System.out.println(simList.get(maxPos)[0] + ", " + simList.get(maxPos2)[0]);
        String aliPath = "/Users/Tobias/Desktop/";//"/home/proj//biocluster/praktikum/genprakt-ws13/abgaben/assignment5/harrert/1c/";
        String[] ali = tm_parser(null, aliPath + "2dkj.A.tm", false);
        DoubleMatrix2D P = (DenseDoubleMatrix2D) readPDB(pdbPath + "1bj4.A.pdb", alignedPositions(ali, true), false);
        DoubleMatrix2D Q = (DenseDoubleMatrix2D) readPDB(pdbPath + simList.get(maxPos)[0] + ".pdb", alignedPositions(ali, false), false);
        DoubleMatrix2D Q_full = (DenseDoubleMatrix2D) readPDB(pdbPath + simList.get(maxPos)[0] + ".pdb", alignedPositions(ali, false), true);
        Object[] sp = Superposition.superimpose(P, Q, Q_full, true);
        PDBParser.matrixToPDB((DenseDoubleMatrix2D)sp[0], PDBParser.pdbToList(aliPath+"2dkj.A.pdb"), "", aliPath, "2dkj_on1bj4.pdb");
    }

    public static void main(String[] args) throws IOException {
//        ArrayList<DoubleMatrix2D> list = multi_superpose("1bj4.A", read_SimList("/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/TMSIM/1bj4.A.simlist"));
        combinedPDB(read_SimList("/Users/Tobias/Desktop/1bj4.A.simlist"));
    }
}
