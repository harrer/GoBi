package blatt5;

import blatt1.Gotoh;
import blatt4.PDBParser;
import static blatt4.PDBParser.alignedPositions;
import static blatt4.PDBParser.params;
import static blatt4.PDBParser.parseToMatrix;
import static blatt4.PDBParser.pdbToSequence;
import blatt4.Superposition;
import static blatt4.TenLongSegments.readcInpairs;
import cern.colt.matrix.DoubleMatrix2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author harrert
 */
public class TM_Align {

    public static String[] tm_parser(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
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

    public static boolean[] tm_alignedPos(String[] alignment, boolean upperSequence, int seqLength) {
        boolean[] b = new boolean[seqLength];
        int c = -1;
        String ali = upperSequence ? alignment[3] : alignment[5];
        for (int i = 0; i < ali.length(); i++) {
            if (ali.charAt(i) != '-') {
                c++;
                if ((alignment[4].charAt(i) == ':') || (alignment[4].charAt(i) == '.')) {
                    b[c] = true;
                }
            }
        }
        return b;
    }

    public static String start(ArrayList<String[]> readcInpairs, String outFile) throws IOException {
        StringBuilder sb = new StringBuilder("P\tQ\tGotoh\tTMalign");
        Gotoh g = new Gotoh(params("dayhoff", "-12", "-1", "freeshift"));
        String pdbPath = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/";//"/Users/Tobias/Desktop/pdb/"
        String tm_path = "/home/proj/biosoft/PROTEINS/CATHSCOP/TMALIGN/";
        int errCount = 0, count = 0, p = 0;
        Superposition s = new Superposition();
        for (String[] seq : readcInpairs) {
            count++;
            if (count >= (p * 558)) {
                System.out.println(p + "%");
                p++;
            }
            String file_p = pdbPath + seq[0] + ".pdb";
            String file_q = pdbPath + seq[1] + ".pdb";
//            try {
                String seq1 = pdbToSequence(file_p), seq2 = pdbToSequence(file_q);
                g.setSequences(seq1, seq2);
                String[] ali = g.backtrackingFreeshift(g.fillMatrixFreeshift());
                DoubleMatrix2D P = parseToMatrix(file_p, alignedPositions(ali, true, seq1.length()), true);
                DoubleMatrix2D Q = parseToMatrix(file_q, alignedPositions(ali, false, seq2.length()), true);
                Object[] superpos_gotoh = s.superimpose(P, Q, null, false);
                ali = tm_parser(tm_path+seq[0]+'-'+seq[1]);
                P = parseToMatrix(file_p, tm_alignedPos(ali, true, Integer.parseInt(ali[0])), true);
                Q = parseToMatrix(file_q, tm_alignedPos(ali, false, Integer.parseInt(ali[1])), true);
                Object[] superpos_tm = s.superimpose(P, Q, null, false);
                sb.append(seq[0]).append('\t').append(seq[1]).append('\t').append(superpos_gotoh[3]).append('\t').append(superpos_tm[3]).append('\n');
//            } catch (Exception e) {
//                //System.out.println(entry.getKey() + ", " + entry.getValue());
//                errCount++;
//            }
        }
        System.out.println(errCount + " errors on " + count);
        PrintWriter writer = new PrintWriter(outFile);
        writer.write(sb.toString());
        writer.close();
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        PDBParser p = new PDBParser();
        ArrayList<String[]> inpairs = readcInpairs("/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/cathscop.inpairs");
        String s = start(inpairs, "/home/h/harrert/Desktop/gotoh_tm_mapping.txt");
    }
}
