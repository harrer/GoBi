package abcd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Combine {

    private final String path_NR = "/home/proj/biosoft/PROTEINS/NR/";
    private final String path_BLAST = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST/";

    public ArrayList d() throws IOException {
        ArrayList<Integer> gi_list = new Tax_to_GI().readFile_HashMap(path_NR + "gi_taxid_prot.dmp").get(9606);
        HashMap<Integer, Boolean> gi_map = new HashMap();
        for (Integer gi : gi_list) {
            gi_map.put(gi, true);
        }
        HashMap<String, NR_Object> NR_map = new NR_BLAST_processor().read_NR_File(path_NR + "nrdump.fasta", gi_map);
        ArrayList b;
        ArrayList<Object[]> result = new ArrayList<>();
        ArrayList<Match_Object> BLAST_list = new ArrayList();
        String[] files = new File(path_BLAST).list();
        long start = new Date().getTime();
        System.out.println("starting find pdb:");
        int c=0,cc=0;
        for (int i=0;i<1000;i++) {//String file : files
            if(100*cc /files.length >= c){
                System.out.println(c+"%");
                c++;
            }
            cc++;
            BLAST_list = new NR_BLAST_processor().read_BLAST_file(path_BLAST + files[i]);
            for (Match_Object match : BLAST_list) {
                b = match.getInfo();
                if (NR_map.containsKey(b.get(0).toString())) {
                    b.add(files[i].substring(0,files[i].length()-6));
                    result.add(b.toArray());
                }
            }
        }
        System.out.println("find all pdb: " + (new Date().getTime() - start) + " ms");
        return result;
    }
    
    public boolean serialize_al(String name, ArrayList<Match_Object> list){
        boolean failure = false;
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name));
            out.writeObject(list);
            out.flush();
            out.close();
        } catch (IOException ex) {
            failure = true;
            System.out.println(ex);
        }
        if(!failure){
            System.out.println("written to "+name);
            return true;
        }
        else{
            System.out.println("failed to write");
            return false;
        }
    }
    
    public ArrayList<Match_Object> unserialize_al(String name){
        ArrayList<Match_Object> list;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(name));
            list = (ArrayList) in.readObject();
            in.close();
            return list;
        } catch (FileNotFoundException ex) {
            System.out.println("Speichersdatei (noch) nicht vorhanden!");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("combine with serialize:");
        long start = new Date().getTime();
        ArrayList<Match_Object> list = new Combine().d();
        new Combine().serialize_al("/tmp/harrert_d_run1", list);
        System.out.println("total " + (new Date().getTime() - start) + "ms");
    }
}
