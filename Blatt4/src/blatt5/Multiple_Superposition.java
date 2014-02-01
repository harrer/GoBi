package blatt5;

import blatt4.PDBParser;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Tobias
 */
public class Multiple_Superposition {

    public static ArrayList<String[]> read_SimList(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] split;
        ArrayList<String[]> list = new ArrayList();
        while ((line = br.readLine()) != null) {
            split = line.split("\\s+");
            list.add(new String[]{split[1],split[5],split[8],split[10],split[12]});//Q, Q.length, ali.length, tm_score, rmsd
        }
        return list;
    }
    
    public static Object[] readPDB(String file) throws IOException{
        PDBParser pdbParser = new PDBParser();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb = new StringBuilder();
        ArrayList<Double[]> coordinates = new ArrayList();
        while ((line = br.readLine()) != null) {
            String[] split = line.split("\\s+");
            if(split[2].equalsIgnoreCase("CA")){
                sb.append(PDBParser.STANDARD_AAS.get(split[3]));
                coordinates.add(new Double[]{Double.parseDouble(split[6]),Double.parseDouble(split[7]),Double.parseDouble(split[8])});
            }
        }
        return new Object[]{sb.toString(), new DenseDoubleMatrix2D(coordinates.toArray(new double[][]{}))};
    }
    
    public static ArrayList<DoubleMatrix2D> multi_superpose(String template, ArrayList<String[]> targets) throws IOException{
        String[] ali = TM_Align.tm_parser("");
        int upper = -1, lower = -1;
        StringBuilder p = new StringBuilder(), q = new StringBuilder();
        for (int i = 0; i < ali[0].length(); i++) {
            if(ali[0].charAt(i))
            if((ali[1].charAt(i) == ':') || (ali[1].charAt(i) == '.')){
                p.append(ali[0].charAt(i));
                q.append(ali[2].charAt(i));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        read_SimList("/Users/Tobias/Desktop/1bj4.A.simlist");
        readPDB("/Users/Tobias/Desktop/pdb/1r5rA00.pdb");
    }
}
