package abcd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Combine {
    
    public ArrayList d() throws IOException{
        ArrayList<Integer> gi_list = new Tax_to_GI().readFile_HashMap("/home/proj/biosoft/PROTEINS/NR/gi_taxid_prot.dmp").get(9606);
        HashMap<Integer, Boolean> gi_map = new HashMap();
        for (Integer gi : gi_list) {
            gi_map.put(gi, true);
        }
        ArrayList<NR_Object> NR_list = new NR_BLAST_processor().read_NR_File("/home/proj/biosoft/PROTEINS/NR/nrdump.fasta", gi_map);
        ArrayList<Match_Object> BLAST_list = new NR_BLAST_processor().read_BLAST_file("/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST/7cat.A.blast");
        Object[] n,b;
        ArrayList<Object[]> result = new ArrayList<>();
        for (NR_Object nr : NR_list) {
            n = nr.getVars();
            for (Match_Object match : BLAST_list) {
                b = match.getInfo();
                if(n[0].equals(b[1]) && n[1].equals(b[2])){
                    System.out.println("hit");
                    result.add(b);
                }
            }
        }
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("combine:");
        long start = new Date().getTime();
        ArrayList list = new Combine().d();
        System.out.println(new Date().getTime() - start + "ms");
    }
}
