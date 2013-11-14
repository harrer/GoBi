package b_c;

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

    private Object[] read_NR_File(String file) throws FileNotFoundException, IOException {   //############ b) ##########
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> sequences = new ArrayList<>();
        HashMap<String, NR_Object> map = new HashMap();
        String line;
        StringBuilder sb = new StringBuilder();
        int index = 0, lines = 56096686, c = 0, h = 0;
        long tStart = new Date().getTime();
        Pattern p1 = Pattern.compile("gi\\|(\\d+)\\|(\\w+)\\|(.*)\\|?.*"), p2 = Pattern.compile("([^>]+)");
        Matcher m1, m2;
        String[] split ,sa;
        while ((line = br.readLine()) != null) {
            if (100.0 * c / lines >= h) {//time measuring
                System.out.println(h + "% " + "completed");
                h++;
            }
            c++;
            if (line.startsWith(">")) {
                //sequences.add(sb.toString());
                //sb.delete(0, sb.length());
                index++;
                m2 = p2.matcher(line);
                while (m2.find()) {
                    m1 = p1.matcher(m2.group(0));
                    m1.find();
                    try{
                        map.put(m1.group(1), new NR_Object("", m1.group(3), m1.group(2), index));
                    }catch(IllegalStateException e){
                        
                    }
                }
            } //else {
                //sb.append(line);
            //}
        }
        long runTime = new Date().getTime() - tStart;
        System.out.println("finished readFile in " + ((runTime / 60000) % 60) + " min " + ((runTime / 1000) % 60) + "s; " + runTime + " ms");
        return new Object[]{map, sequences};
    }

    private ArrayList<NR_Object> nr_objects_list(HashMap<String, NR_Object> nr_map, ArrayList sequences, ArrayList<Integer> gi) {
        ArrayList<NR_Object> list = new ArrayList();
        Object[] nro;
        for (int id : gi) {
            nro = nr_map.get("" + id).getVars();
            list.add(new NR_Object(nro[0].toString(), nro[1].toString(), nro[2].toString(), Integer.parseInt(nro[3].toString())));
        }
        return list;
    }

    private void split(String[] sa, String in, char split, StringBuilder sb) {
        sb.delete(0, sb.length());
        int c = 0;
        for (int i = 0; i < in.length(); i++) {
            if (in.charAt(i) == split) {
                sa[c] = sb.toString();
                c++;
                sb.delete(0, sb.length());
            } else {
                sb.append(in.charAt(i));
            }
        }
        sa[c] = sb.toString();
        sa[c + 1] = ">>>##END##<<<";
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
        //new NR_BLAST_processor().read_BLAST_file(args[0]);
        new NR_BLAST_processor().read_NR_File("/home/proj/biosoft/PROTEINS/NR/nrdump.fasta");//args[0]
    }
}
