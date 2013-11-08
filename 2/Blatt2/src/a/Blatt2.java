package a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author harrert
 */
public class Blatt2 {

    public static void readFile(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String[] split;
        int min = Integer.MAX_VALUE, max = -1;
        int currentTaxID = 0;
        String line;
        while((line = br.readLine()) != null){
            split = line.split("\t");
            currentTaxID = Integer.parseInt(split[1]);
            min = currentTaxID < min ? currentTaxID : min;
            max = currentTaxID > max ? currentTaxID : max;
        }
        System.out.println("min: "+min+" max: "+max);
        ArrayList[] a = new ArrayList[max+1];
        for (int i=0; i<a.length;i++) {
            a[i] = new ArrayList();
        }
        fr.close();br.close();
        fr = new FileReader(file);
        br = new BufferedReader(fr);
        while((line = br.readLine()) != null){
            split = line.split("\t");
            a[Integer.parseInt(split[1])].add(split[0].toString());
        }
        fr.close();
        br.close();
        ArrayList l = a[1289135];
        for (Object s : l) {
            System.out.println(s);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        readFile("/home/proj/biosoft/PROTEINS/NR/gi_taxid_prot.dmp");
    }
    
}
