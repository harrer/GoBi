package blatt4;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author harrert
 */
public class PDBParser {

    public static DoubleMatrix2D parseToMatrix(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int columns = 0;
        ArrayList<Double[]> list = new ArrayList<>(1000);
        while ((line = br.readLine()) != null) {
            if (line.startsWith("ATOM")) {
                columns++;
                String[] split = line.split("\\s+");
                list.add(new Double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
            }
        }
        br.close();
        double[][] da = list.toArray(new double[columns][3]);
        System.out.println("");
        return new DenseDoubleMatrix2D(da);
    }

    public static ArrayList<AminoAcid> parseAll(String file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] split;
        int aaCount = -1, end = -2;
        ArrayList<AminoAcid> list = new ArrayList<>();
        ArrayList<Double[]> posList = new ArrayList<>();
        ArrayList<String> atomList = new ArrayList<>();
        AminoAcid current = new AminoAcid(aaCount, -1, "");
        while ((line = br.readLine()) != null) {
            if (line.startsWith("ATOM")) {
                split = line.split("\\s+");
                if (Integer.parseInt(split[5]) != aaCount) {
                    current.setCoordinates(new DenseDoubleMatrix2D(posList.toArray(new double[][]{})));
                    current.setEndPos(end);
                    current.setAtomNames(atomList);
                    list.add(current);
                    current = new AminoAcid(Integer.parseInt(split[5]), Integer.parseInt(split[1]), split[3]);
                    posList = new ArrayList<>();
                    aaCount = Integer.parseInt(split[5]);
                }
                end = Integer.parseInt(split[0]);
                atomList.add(split[2]);
                posList.add(new Double[]{Double.parseDouble(split[6]), Double.parseDouble(split[7]), Double.parseDouble(split[8])});
            }
        }
        list.remove(0);
        fr.close();
        br.close();
        return list;
    }

    public static void main(String[] args) throws IOException {
        String file = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/1ev0B00.pdb";
        DoubleMatrix2D matrix = parseToMatrix(file);
        System.out.println("");
        //ArrayList<AminoAcid> aaList = parseAll(file);
    }
}
