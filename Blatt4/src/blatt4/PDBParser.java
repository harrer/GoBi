package blatt4;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import blatt1.*;
import static blatt4.TenLongSegments.readcInpairs;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author harrert
 */
public class PDBParser {

    private final static HashMap<String, String> STANDARD_AAS = new HashMap<>();

    public PDBParser() {
        STANDARD_AAS.put("ALA", "A");
        STANDARD_AAS.put("ARG", "R");
        STANDARD_AAS.put("ASN", "N");
        STANDARD_AAS.put("ASP", "D");
        STANDARD_AAS.put("CYS", "C");
        STANDARD_AAS.put("GLU", "E");
        STANDARD_AAS.put("GLN", "Q");
        STANDARD_AAS.put("GLY", "G");
        STANDARD_AAS.put("HIS", "H");
        STANDARD_AAS.put("ILE", "I");
        STANDARD_AAS.put("LEU", "L");
        STANDARD_AAS.put("LYS", "K");
        STANDARD_AAS.put("MET", "M");
        STANDARD_AAS.put("PHE", "F");
        STANDARD_AAS.put("PRO", "P");
        STANDARD_AAS.put("SER", "S");
        STANDARD_AAS.put("THR", "T");
        STANDARD_AAS.put("VAL", "V");
        STANDARD_AAS.put("TYR", "Y");
        STANDARD_AAS.put("TRP", "W");
        int s = STANDARD_AAS.size();
        HashMap<String, String> tmp = new HashMap(STANDARD_AAS.size());
        for (Map.Entry<String, String> entry : STANDARD_AAS.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            tmp.put(value, key);
        }
        for (Map.Entry<String, String> entry : tmp.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            STANDARD_AAS.put(k, v);
        }
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

    public static String matrixToPDB(DoubleMatrix2D matrix, String sequence, String path_out, String name) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder("REMARK\nREMARK Protein ");
        DecimalFormat dec = new DecimalFormat("#0.000", new DecimalFormatSymbols(Locale.US));
        sb.append(name).append("\nREMARK File written by harrert\nREMARK\n");
        for (int i = 0; i < matrix.rows(); i++) {
            sb.append("ATOM   ").append(i).append("   CA   ").append(STANDARD_AAS.get(sequence.substring(i, i + 1))).append("   B   ").append(i);
            sb.append("   ").append(dec.format(matrix.get(i, 0))).append("   ").append(dec.format(matrix.get(i, 1))).append("   ").append(dec.format(matrix.get(i, 2))).append("\n");
        }
        sb.append("TER");
        PrintWriter writer = new PrintWriter(path_out + name);
        writer.write(sb.toString());
        writer.close();
        return sb.toString();
    }

    public static String alignedSequence(String sequence, boolean[] alignedPositions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sequence.length(); i++) {
            if (alignedPositions[i]) {
                sb.append(sequence.charAt(i));
            }
        }
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

    private static boolean[] alignedPositions(String[] alignment, boolean upperSequence, int seqLength) {
        boolean[] b = new boolean[seqLength];
        int c = 0;
        if (upperSequence) {
            for (int i = 0; i < alignment[0].length(); i++) {
                if ((alignment[0].charAt(i) != '-') && (alignment[1].charAt(i) != '-')) {
                    b[c] = true;
                    c++;
                } else if ((alignment[0].charAt(i) != '-') && (alignment[1].charAt(i) == '-')) {
                    b[c] = false;
                    c++;
                }
            }
        } else {
            for (int i = 0; i < alignment[0].length(); i++) {
                if ((alignment[0].charAt(i) != '-') && (alignment[1].charAt(i) != '-')) {
                    b[c] = true;
                    c++;
                } else if ((alignment[1].charAt(i) != '-') && (alignment[0].charAt(i) == '-')) {
                    b[c] = false;
                    c++;
                }
            }
        }
//        for (int i = 0; i < alignment[0].length() - b.size(); i++) {
//            b.add(Boolean.FALSE);
//        }
        return b;
    }

    public static Object[] start(HashMap<String, String> readcInpairs) throws IOException {
        HashMap<Double, ArrayList<Double>> rmsd_map = new HashMap<>();
        HashMap<Double, ArrayList<Double>> gtdTS_map = new HashMap<>();
        Gotoh g = new Gotoh(params("dayhoff", "-12", "-1", "freeshift"));
        String pdbPath = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/";
        for (Map.Entry<String, String> entry : readcInpairs.entrySet()) {
            String file_p = pdbPath + entry.getKey() + ".pdb";
            String file_q = pdbPath + entry.getValue() + ".pdb";
            String seq1 = pdbToSequence(file_p), seq2 = pdbToSequence(file_q);
            g.setSequences(seq1, seq2);
            String[] ali = g.backtrackingFreeshift(g.fillMatrixFreeshift());
            DoubleMatrix2D P = parseToMatrix(file_p, alignedPositions(ali, true, seq1.length()));
            DoubleMatrix2D Q = parseToMatrix(file_q, alignedPositions(ali, false, seq2.length()));
            Superposition s = new Superposition();
            Object[] superposition = s.superimpose(P, Q);
            ArrayList<Double> rTmp = (rmsd_map.containsKey(1.0 * P.rows())) ? rmsd_map.get(1.0 * P.rows()) : new ArrayList<Double>();
            rTmp.add((Double) superposition[2]);
            ArrayList<Double> gTmp = (gtdTS_map.containsKey(1.0 * P.rows())) ? gtdTS_map.get(1.0 * P.rows()) : new ArrayList<Double>();
            gTmp.add((Double) superposition[3]);
            rmsd_map.put(1.0 * P.rows(), rTmp);
            gtdTS_map.put(1.0 * P.rows(), gTmp);
        }
        return new Object[]{rmsd_map, gtdTS_map};
    }

    public static void main(String[] args) throws IOException {
        long timeBefore = new Date().getTime();
        PDBParser p = new PDBParser();
//        String file_p = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/1wq2B00.pdb";// "/home/tobias/Documents/GoBi/Blatt4/1ev0B00.pdb";
//        String file_q = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/1lddB00.pdb";//"/Users/Tobias/Desktop/pdb/1wq2B00.pdb";
//        System.out.println("gdt-ts: "+s.gdt_ts(P, (DoubleMatrix2D)superposition[0]));
//        matrixToPDB((DoubleMatrix2D)superposition[0], alignedSequence(seq2, b), "/Users/Tobias/Desktop/", "out.pdb");
        HashMap<String, String> inpairs = readcInpairs("/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/cathscop.inpairs");
        Object[] sp = start(inpairs);
        System.out.println(new Date().getTime() - timeBefore + " ms");
    }
}
