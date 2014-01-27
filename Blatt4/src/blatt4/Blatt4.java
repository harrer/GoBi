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
public class Blatt4 {

    public static ArrayList<DoubleMatrix2D> tenLongSegments(String file) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int aaCount = -1;
        String[] split;
        ArrayList<DoubleMatrix2D> list = new ArrayList();
        while ((line = br.readLine()) != null) {
            //DoubleMatrix2D m = new DenseDoubleMatrix2D
            split = line.split("\\t");
        }
        br.close();
        return list;
    }
    
    public static void main(String[] args) {
        int[] a = {1,2,3,345,3545,35,3415};
        int[] b = {14,22,311,12431};
        int[][] ab = {a,b};
        System.out.println(ab.length);
    }
    
}
