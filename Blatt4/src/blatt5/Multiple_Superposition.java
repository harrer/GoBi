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
    
    public static String[] tm_parser(BufferedReader br) throws IOException {
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

    public static Object[] readPDB(String file, ArrayList<Boolean> aligned) throws IOException {
        PDBParser pdbParser = new PDBParser();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb = new StringBuilder();
        ArrayList<Double[]> coordinates = new ArrayList();
        int c = -1;
        while ((line = br.readLine()) != null) {
            c++;
            String[] split = line.split("\\s+");
            if (aligned.get(c).booleanValue() && split[2].equalsIgnoreCase("CA")) {
                sb.append(PDBParser.STANDARD_AAS.get(split[3]));
                coordinates.add(new Double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
            }
        }
        return new Object[]{sb.toString(), new DenseDoubleMatrix2D(coordinates.toArray(new double[][]{}))};
    }

    public static ArrayList<DoubleMatrix2D> multi_superpose(String template, ArrayList<String[]> targets) throws IOException {
        ArrayList<Boolean> upper = new ArrayList<>(), lower = new ArrayList<>();
        ArrayList<DoubleMatrix2D> list = new ArrayList();
        String pdbPath = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/STRUCTURES/";
        for (String[] s : targets) {
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cd /home/proj/biosoft/PROTEINS/software/; ./TMalign "+pdbPath+template+".pdb "+pdbPath+s+".pdb").getInputStream()));
            String[] ali = tm_parser(br);
            br.close();
            for (int i = 0; i < ali[0].length(); i++) {
                if (ali[0].charAt(i) != '-') {
                    if((ali[1].charAt(i) == ':') || (ali[1].charAt(i) == '.')){
                        upper.add(Boolean.TRUE);
                    }
                    else{
                        upper.add(Boolean.FALSE);
                    }
                }
                if (ali[2].charAt(i) != '-') {
                    if((ali[1].charAt(i) == ':') || (ali[1].charAt(i) == '.')){
                        lower.add(Boolean.TRUE);
                    }
                    else{
                        lower.add(Boolean.FALSE);
                    }
                }
                DoubleMatrix2D P = (DenseDoubleMatrix2D) readPDB(template, upper)[1];
                DoubleMatrix2D Q = (DenseDoubleMatrix2D) readPDB(template, lower)[1];
                Object [] sp = Superposition.superimpose(P, Q, null, false);
                list.add((DoubleMatrix2D) sp[0]);
            }
        }
        return list;
    }

    public static void combinedPDB(ArrayList<String[]> simList) {
        double max = -1;
        int maxPos = -1;
        for (int i = 0; i < simList.size(); i++) {
            double d = Double.parseDouble(simList.get(i)[3]);
            if (d > max) {
                max = d;
                maxPos = i;
            }
        }
        double max2 = -1;
        int maxPos2 = -1;
        for (int i = 0; i < simList.size(); i++) {
            double d = Double.parseDouble(simList.get(i)[3]);
            if (d > max2 && i != maxPos) {
                max2 = d;
                maxPos2 = i;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<DoubleMatrix2D> list = multi_superpose("1bj4.A", read_SimList("/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/TMSIM/1bj4.A.simlist"));//"/Users/Tobias/Desktop/1bj4.A.simlist"
        
    }
}
