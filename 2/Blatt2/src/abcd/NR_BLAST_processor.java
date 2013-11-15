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

    //############ b) ##########
    public HashMap<String, NR_Object> read_NR_File(String file, HashMap gi_list) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        //ArrayList<NR_Object> results = new ArrayList();
        HashMap<String, NR_Object> results = new HashMap();
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
                sa = line.split(">")[1].split("\\|");
                if (sa.length > 1 && sa[1].matches("\\d+") && gi_list.containsKey(Integer.parseInt(sa[1]))) {
                    results.put(sa[3], new NR_Object("", sa[3], sa[2]));
                }
            }
        }
        long runTime = new Date().getTime() - tStart;
        System.out.println(results.size());
        System.out.println("finished readFile in " + ((runTime / 60000) % 60) + " min " + ((runTime / 1000) % 60) + "s; " + runTime + " ms");
        return results;
    }

    //######### c) #########
    public ArrayList read_BLAST_file(String file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] split_pipe, split_tab;
        ArrayList<Match_Object> list = new ArrayList<>();
        int round = 0;
        Pattern p = Pattern.compile(".+(\\.{3}|])\\s+(\\d+)\\s+(\\d?e-\\d+)");
        Matcher m;
        Double e_value;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith(">") && !line.startsWith(" ") && line.matches(".+\\|.+\\|.+")) {
                split_pipe = line.split("\\|");
                m = p.matcher(line);
                while (m.find()) {
                    e_value = m.group(3).matches("e-\\d+") ? Double.parseDouble("1" + m.group(3)) : Double.parseDouble(m.group(3));
                    list.add(new Match_Object(split_pipe[1], split_pipe[0], e_value, Integer.parseInt(m.group(2)), round));
                }
            } else if (line.matches("Results from round.+")) {
                round++;
            }
        }
        fr.close(); br.close();
        return list;
    }

    public static void main(String[] args) throws IOException {
//        HashMap<Integer, Boolean> map = new HashMap();
//        map.put(23201, true);map.put(23691, true);map.put(13014, true);map.put(23907, true);
//        new NR_BLAST_processor().read_NR_File("/home/proj/biosoft/PROTEINS/NR/nrdump.fasta", map);
        new NR_BLAST_processor().read_BLAST_file("/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST/7cat.A.blast");

    }
}
