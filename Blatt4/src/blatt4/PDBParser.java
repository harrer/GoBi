package blatt4;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import blatt1.*;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class PDBParser {

    private final static HashMap<String, Character> STANDARD_AAS = new HashMap<>();

    public PDBParser() {
        STANDARD_AAS.put("ALA", 'A');
        STANDARD_AAS.put("ARG", 'R');
        STANDARD_AAS.put("ASN", 'N');
        STANDARD_AAS.put("ASP", 'D');
        STANDARD_AAS.put("CYS", 'C');
        STANDARD_AAS.put("GLU", 'E');
        STANDARD_AAS.put("GLN", 'Q');
        STANDARD_AAS.put("GLY", 'G');
        STANDARD_AAS.put("HIS", 'H');
        STANDARD_AAS.put("ILE", 'I');
        STANDARD_AAS.put("LEU", 'L');
        STANDARD_AAS.put("LYS", 'K');
        STANDARD_AAS.put("MET", 'M');
        STANDARD_AAS.put("PHE", 'F');
        STANDARD_AAS.put("PRO", 'P');
        STANDARD_AAS.put("SER", 'S');
        STANDARD_AAS.put("THR", 'T');
        STANDARD_AAS.put("VAL", 'V');
        STANDARD_AAS.put("TYR", 'Y');
        STANDARD_AAS.put("TRP", 'W');
    }

    public static DoubleMatrix2D parseToMatrix(String file, boolean[] allignedPositions) throws IOException {//, int positions
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int aaCount = -1;
        ArrayList<double[]> list = new ArrayList();
        while ((line = br.readLine()) != null) {
            if (line.startsWith("ATOM")) {
                String[] split = line.split("\\s+");
                int cc = Integer.parseInt(split[5]);
                if (allignedPositions[cc] && split[2].equalsIgnoreCase("CA")) {//split[2].equalsIgnoreCase("N") || split[2].equalsIgnoreCase("CA") || split[2].equalsIgnoreCase("C")
                    list.add(new double[]{Double.parseDouble(split[5]), Double.parseDouble(split[6]), Double.parseDouble(split[7])});
                }
            }
        }
        br.close();
        double[][] dOut = list.toArray(new double[list.size()][3]);
        return new DenseDoubleMatrix2D(dOut);
    }

    public static ArrayList<AminoAcid> parseAll(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int aaCount = -1, end = -2;
        ArrayList<AminoAcid> list = new ArrayList<>();
        ArrayList<double[]> posList = new ArrayList<>();
        ArrayList<String> atomList = new ArrayList<>();
        AminoAcid current = new AminoAcid(aaCount, -1, "", "");
        while ((line = br.readLine()) != null) {
            if (line.startsWith("ATOM")) {
                String[] split = line.split("\\s+");
                if (Integer.parseInt(split[5]) != aaCount) {
                    current.setCoordinates(new DenseDoubleMatrix2D(posList.toArray(new double[][]{})));
                    current.setEndPos(end);
                    current.setAtomNames(atomList);
                    atomList = new ArrayList();
                    list.add(current);
                    current = new AminoAcid(Integer.parseInt(split[5]), Integer.parseInt(split[1]), split[3], split[4]);
                    posList = new ArrayList();
                    aaCount = Integer.parseInt(split[5]);
                }
                end = Integer.parseInt(split[1]);
                atomList.add(split[2]);
                if (split[2].equalsIgnoreCase("CA")) {
                    current.setcAlpha(posList.size());
                } else if (split[2].equalsIgnoreCase("C")) {
                    current.setC(posList.size());
                } else if (split[2].equalsIgnoreCase("N")) {
                    current.setN(posList.size());
                }
                posList.add(new double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
            }
        }
        current.setCoordinates(new DenseDoubleMatrix2D(posList.toArray(new double[][]{})));
        current.setEndPos(end);
        current.setAtomNames(atomList);
        list.add(current);
        list.remove(0);
        br.close();
        return list;
    }

    private static String pdbToSequence(String file) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int aaCount = -1;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("ATOM")) {
                String[] split = line.split("\\s+");
                int cc = Integer.parseInt(split[5]);
                if (cc != aaCount) {
                    aaCount = cc;
                    sb.append(STANDARD_AAS.get(split[3]));
                }
            }
        }
        br.close();
        return sb.toString();
    }

    private static HashMap<String, String> params(String matrix, String go, String ge, String mode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("-m", matrix);//"BlakeCohenMatrix|dayhoff|THREADERSimilarityMatrix|blosum50|pam250"
        params.put("-go", go);
        params.put("-ge", ge);
        params.put("-mode", mode);//"local|global|freeshift"
        return params;
    }

    private static boolean[] alignedPositions(String[] alignment) {
        boolean[] b = new boolean[alignment[0].length()];
        for (int i = 0; i < alignment[0].length(); i++) {
            b[i] = ((alignment[0].charAt(i) != '-') && (alignment[1].charAt(i) != '-'));
        }
        return b;
    }

    public static void main(String[] args) throws IOException {
        long timeBefore = new Date().getTime();
        PDBParser p = new PDBParser();
        String file = "/Users/Tobias/Desktop/pdb/1wq2B00.pdb";//"/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/1ev0B00.pdb"; "/home/tobias/Documents/GoBi/Blatt4/1ev0B00.pdb";
        String file2 = "/Users/Tobias/Desktop/pdb/1lddB00.pdb";
        String seq1 = pdbToSequence(file), seq2 = pdbToSequence(file2);
        Gotoh g = new Gotoh(params("dayhoff", "-12", "-1", "freeshift"));
        g.setSequences(seq1, seq2);
        String[] ali = g.backtrackingFreeshift(g.fillMatrixFreeshift());
        boolean[] b = alignedPositions(ali);
        DoubleMatrix2D matrix = parseToMatrix(file,b);
        DoubleMatrix2D matrix2 = parseToMatrix(file2,b);
        Superposition s = new Superposition(matrix, matrix2);
        System.out.println(new Date().getTime() - timeBefore + " ms");
    }
}
