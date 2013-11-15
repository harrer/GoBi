package abcd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
    private String file;

    public ArrayList<Object[]> d() throws IOException {
        // a) #####
        ArrayList<Integer> gi_list = new Tax_to_GI().readFile_HashMap(path_NR + "gi_taxid_prot.dmp").get(9606);
        HashMap<Integer, Boolean> gi_map = new HashMap();
        for (Integer gi : gi_list) {
            gi_map.put(gi, true);
        }
        // b) #####
        HashMap<String, NR_Object> NR_map = new NR_BLAST_processor().read_NR_File(path_NR + "nrdump.fasta", gi_map);
        ArrayList b;
        ArrayList<Object[]> result = new ArrayList<>();
        ArrayList<Match_Object> BLAST_list;
        String[] files = new File(path_BLAST).list();
        long start = new Date().getTime();
        System.out.println("starting find pdb:");
        int c=0,cc=0;
        // d) #####
        for (int i=0;i<1000;i++) {//String file : files
            if(100*cc /files.length >= c){
                System.out.println(c+"%");
                c++;
            }
            cc++;
            // c) #####
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
    
    public void writeToFile(ArrayList<Object[]> list, String path) throws FileNotFoundException, IOException{
        StringBuilder sb = new StringBuilder();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
        String tab = "\t", newline = "\n";
        for (Object[] object : list) {
            for (Object obj : object) {
                sb.append(obj);
                sb.append(tab);
            }
            sb.append(newline);
        }
        writer.write(sb.toString());
        writer.close();
    }
    
    public ArrayList<String[]> read_ArrayList(String path) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String[]> list = new ArrayList();
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line.split("\t"));
        }
        fr.close(); fr.close();
        return list;
    }

    public static void main(String[] args) throws IOException {
        Combine d = new Combine();
        Enrich_Ensembl e = new Enrich_Ensembl();
        System.out.println("combine, save mapping:");
        long start = new Date().getTime();
        ArrayList<Object[]> list = d.d();
        d.writeToFile(list, "/tmp/harrert_mapping_gobi_17_42");
        // e) #####
        HashMap<String, Boolean> map = e.read_mart_export();
        System.out.println("total " + (new Date().getTime() - start) + "ms");
    }
}
