package a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Tax_to_GI {

    public static void readFileWithArray(String file) throws FileNotFoundException, IOException{
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
        ArrayList<Integer>[] a = new ArrayList[max+1];
        for (int i=0; i<a.length;i++) {
            a[i] = new ArrayList<Integer>();
        }
        fr.close();br.close();
        fr = new FileReader(file);
        br = new BufferedReader(fr);
        while((line = br.readLine()) != null){
            split = line.split("\t");
            a[Integer.parseInt(split[1])].add(Integer.parseInt(split[0]));
        }
        fr.close();
        br.close();
        ArrayList l = a[1289135];
        for (Object s : l) {
            System.out.println(s);
        }
    }
    
    public HashMap readFile_HashMap(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String[] split;
        String line;
        HashMap<Integer, ArrayList<Integer>> map = new HashMap();
        while((line = br.readLine()) != null){
            split = line.split("\t");
            int key = Integer.parseInt(split[1]);
            if(map.containsKey(key)){
                map.get(key).add(Integer.parseInt(split[0]));
            }
            else{
                ArrayList al = new ArrayList<Integer>();
                al.add(Integer.parseInt(split[0]));
                map.put(key, al);
            }
        }
        fr.close();
        br.close();
//        ArrayList l = map.get(1289135);
//        for (Object s : l) {
//            System.out.println(s);
//        }
//        System.out.println(map.get(9606).size());
        return map;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //readFileWithArray("/home/proj/biosoft/PROTEINS/NR/gi_taxid_prot.dmp");
        new Tax_to_GI().readFile_HashMap("/home/proj/biosoft/PROTEINS/NR/gi_taxid_prot.dmp");
        //readFile("/home/h/harrert/Dropbox/UNI/GoBi/Blatt 2/1289135");
    }
    
}
