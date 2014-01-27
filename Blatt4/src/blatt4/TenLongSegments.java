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
public class TenLongSegments {

    public static ArrayList<DoubleMatrix2D> tenLongSegments(String file) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] split;
        ArrayList<double[]> backbone = new ArrayList();
        while ((line = br.readLine()) != null) {
            split = line.split("\\t");
            backbone.add(new double[]{Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])});
        }
        br.close();
        double[][] bbArray = backbone.toArray(new double[0][0]);
        ArrayList<DoubleMatrix2D> tenLongs = new ArrayList<>();
        for (int i = 0; i <= backbone.size()-10; i++) {
            double[][] tmp = new double[10][3];
            for (int j = 0; j < tmp.length; j++) {
                tmp[j] = bbArray[i+j];
            }
            tenLongs.add(new DenseDoubleMatrix2D(tmp));
        }
        return tenLongs;
    }
    
    public static void main(String[] args) throws IOException {
        String file = "/Users/Tobias/Dropbox/UNI/GoBi/Blatt 4/1r5ra00.backbone";
        ArrayList<DoubleMatrix2D> m = tenLongSegments(file);
        DoubleMatrix2D P = m.get(0);
        DoubleMatrix2D Q = m.get(3);
        Superposition s = new Superposition(P, Q);
    }
    
}
