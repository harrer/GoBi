package abcd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author harrert
 */
public class NR_BLAST_processor {

    private ArrayList read_NR_File(String file, HashMap gi_list) throws FileNotFoundException, IOException {   //############ b) ##########
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<NR_Object> results = new ArrayList<>();
        String line;
        String[] split, sa;
        int index = 0, lines = 56096686, c = 0, h = 0;
        long tStart = new Date().getTime();
        while ((line = br.readLine()) != null) {
            if (100.0 * c / lines >= h) {//time measuring
                System.out.println(h + "% " + "completed");
                h++;
            }
            c++;
            if (line.startsWith(">")) {
                split = line.split(">");
                for (String gi : split) {
                    sa = gi.split("\\|");
                    if (sa.length > 1 && gi_list.containsKey(sa[1])) {
                        results.add(new NR_Object("", sa[3], sa[2]));
                    }
                }
            }
        }
        long runTime = new Date().getTime() - tStart;
        System.out.println("finished readFile in " + ((runTime / 60000) % 60) + " min " + ((runTime / 1000) % 60) + "s; " + runTime + " ms");
        return results;
    }

    private ArrayList read_BLAST_file(String file) throws FileNotFoundException, IOException {   //######### c) #########
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] split_pipe, split_tab;
        ArrayList<Match_Object> list = new ArrayList<>();
        int round = 0;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith(">") && !line.startsWith(" ") && line.matches(".+\\|.+\\|.+")) {
                split_pipe = line.split("\\|");
                split_tab = line.split("\\s{2,}");
                Double d = split_tab[2].matches("e-\\d+") ? Double.parseDouble("1" + split_tab[2]) : Double.parseDouble(split_tab[2]);
                list.add(new Match_Object(split_pipe[1], split_pipe[0], d, Integer.parseInt(split_tab[1]), round));
            } else if (line.matches("Results from round.+")) {
                round++;
            }
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
//        HashMap<Integer, Boolean> map = new HashMap();
//        map.put(23201, true);map.put(23691, true);map.put(13014, true);map.put(23907, true);
//        new NR_BLAST_processor().read_NR_File("/home/proj/biosoft/PROTEINS/NR/nrdump.fasta", map);
        new NR_BLAST_processor().read_BLAST_file("/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST/7cat.A.blast");
        
    }
}
