package abcd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author harrert
 */
public class Combine {
    
    private final String path_NR = "/home/proj/biosoft/PROTEINS/NR/";
    private final String path_BLAST = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST/";
    
    public ArrayList d() throws IOException{
        ArrayList<Integer> gi_list = new Tax_to_GI().readFile_HashMap(path_NR+"gi_taxid_prot.dmp").get(9606);
        HashMap<Integer, Boolean> gi_map = new HashMap();
        for (Integer gi : gi_list) {
            gi_map.put(gi, true);
        }
        ArrayList<NR_Object> NR_list = new NR_BLAST_processor().read_NR_File(path_NR+"nrdump.fasta", gi_map);
        Object[] n,b;
        ArrayList<Object[]> result = new ArrayList<>();
        ArrayList<Match_Object> BLAST_list = new ArrayList();
        String[] files = new File(path_BLAST).list();
        long start = new Date().getTime();
        System.out.println("starting find pdb:");
        for (int i=0; i< 1000; i++) {//String blast_file : files
            //if()
            try {
                BLAST_list = new NR_BLAST_processor().read_BLAST_file(path_BLAST + files[i]);
            } catch (NumberFormatException numberFormatException) {
                System.out.println(files[i]);
            }
            for (Match_Object match : BLAST_list) {
                b = match.getInfo();
                if (b[1].toString().equalsIgnoreCase("pdb")) {
                    for (NR_Object nr : NR_list) {
                        n = nr.getVars();
                        if (n[1].equals(b[0])) {
                            result.add(b);
                        }
                    }
                }
            }
        }
        System.out.println("find all pdb: "+ (new Date().getTime() - start) +" ms");
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("combine:");
        long start = new Date().getTime();
        ArrayList list = new Combine().d();
        System.out.println("total "+ (new Date().getTime() - start) + "ms");
    }
}
