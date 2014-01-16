package blatt4;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author harrert
 */
public class PDBParser {

    public static DoubleMatrix2D[] parseToMatrix(String file, boolean backboneOnly) throws IOException {//, int positions
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        ArrayList<double[]> list = new ArrayList();
        while ((line = br.readLine()) != null) {
            if (line.startsWith("ATOM")) {
                String[] split = line.split("\\s+");
                if (backboneOnly) {
                    if (split[2].equalsIgnoreCase("N") || split[2].equalsIgnoreCase("CA") || split[2].equalsIgnoreCase("C")) {
                        list.add(new double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
                    }
                } else {
                    list.add(new double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
                }
            }
        }
        //double[][] da = list.toArray(new double[list.size()][3]);
        br.close();
        double[][] dOut = list.toArray(new double[list.size()][3]);
        //if(list.size()<=positions){
            return new DoubleMatrix2D[]{new DenseDoubleMatrix2D(dOut)};
        //}
//        else{
//            int lists = list.size()/positions + 1;
//            DoubleMatrix2D[] out = new DoubleMatrix2D[lists];
//            for (int i = 0; i < lists; i++) {
//                double[][] d2 = new double[positions][3];
//                for (int j = 0; j < d2.length; j++) {
//                    if((i*positions + j) < list.size()){
//                        d2[j] = dOut[i*positions + j];
//                    }
//                }
//                out[i] = new DenseDoubleMatrix2D(d2);
//            }
//            return out;
//        }
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

    public static void main(String[] args) throws IOException {
        String file = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/1ev0B00.pdb"; //"/home/tobias/Documents/GoBi/Blatt4/1ev0B00.pdb";
//        DoubleMatrix2D[] matrix = parseToMatrix(file, true, 10);
        ArrayList<AminoAcid> aaList = parseAll(file);
    }
}
