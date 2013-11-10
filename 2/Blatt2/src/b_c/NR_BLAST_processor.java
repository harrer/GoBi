package b_c;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class NR_BLAST_processor {
    
    private void read_NR_File(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<NR_Object> nr_objects = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap();
        String line;
        String[] split;
        int index = -1 ,lines = 56096686, c=0, h=0;
        long tStart = new Date().getTime();
        boolean newEntry = true;
        while ((line = br.readLine()) != null) {
            if(100.0*c/lines >= h){//time measuring
                System.out.println(h+"% "+"completed");
                h++;
            }
            c++;
            if(line.startsWith(">")){
                newEntry = true;
                index++;
                split = line.split(">");
                for (int i = 1; i < split.length; i++) {
                    String [] sa = split[i].split("|");
                    map.put(sa[1], index);
                }
            }
            else{
                if(newEntry){
                    nr_objects.add(new NR_Object(line, "", ""));
                    newEntry = false;
                }
                else{
                    nr_objects.get(index).sequence += line;
                }
            }
        }
        long runTime = new Date().getTime() - tStart;
        System.out.println("finished in "+(runTime/60000)+" min "+(runTime/1000)+"s; "+runTime+" ms");
    }
    
    private void read_BLAST_file(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] split_pipe, split_tab = new String[100];
        ArrayList<Match_Object> list = new ArrayList<>();
        int round = 0;
        while((line = br.readLine()) != null){
            if(line.matches(".+\\|.+\\|.+")){
                split_pipe = line.split("\\|");
                try {
                    split_tab = line.split("\\s{2,}");
                } catch (Exception e) {
                    System.out.println(line);
                }
                double d = split_tab[2].matches("e-\\d+")? Double.parseDouble("1"+split_tab[2]) : Double.parseDouble(split_tab[2]);
                list.add(new Match_Object(split_pipe[1], split_pipe[0], d, Integer.parseInt(split_tab[1]), round));
            }
            else if(line.matches("Results from round.+")){
                round ++;
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        new NR_BLAST_processor().read_BLAST_file(args[0]);//"/home/tobias/Dropbox/UNI/GoBi/Blatt 2/blast"
    }
}
