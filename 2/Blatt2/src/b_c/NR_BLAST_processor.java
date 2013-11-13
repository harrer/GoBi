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
    
    private Object[] read_NR_File(String file) throws FileNotFoundException, IOException{   //############ b) ##########
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> sequences = new ArrayList<>();
        HashMap<String,NR_Object> map = new HashMap();
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
                 String [] sa;
                for (int i = 1; i < split.length; i++) {
                    if(split[i].matches(".+\\|.+")){
                        sa = split[i].split("|");
                        map.put(sa[1], new NR_Object("", sa[3], sa[2], index));
                        sa = null;// REGEX!1 #############################################
                    }
                }
                split = null;
            }
            else{
                if(newEntry){
                    sequences.add(line);
                    newEntry = false;
                }
                else{
                    sequences.set(index, sequences.get(index) + line);
                }
            }
        }
        long runTime = new Date().getTime() - tStart;
        System.out.println("finished readFile in "+(runTime/60000)+" min "+(runTime/1000)+"s; "+runTime+" ms");
        return new Object[]{map, sequences};
    }
    
    private ArrayList<NR_Object> nr_objects_list(HashMap<String,NR_Object> nr_map, ArrayList sequences, ArrayList<Integer> gi){
        ArrayList<NR_Object> list = new ArrayList();
        Object[] nro;
        for (int id : gi) {
            nro = nr_map.get(""+id).getVars();
            list.add(new NR_Object(nro[0].toString(), nro[1].toString(), nro[2].toString(), Integer.parseInt(nro[3].toString())));
        }
        return list;
    }
    
    private ArrayList read_BLAST_file(String file) throws FileNotFoundException, IOException{   //######### c) #########
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] split_pipe, split_tab;
        ArrayList<Match_Object> list = new ArrayList<>();
        int round = 0;
        while((line = br.readLine()) != null){
            if(!line.startsWith(">") && !line.startsWith(" ") && line.matches(".+\\|.+\\|.+")){
                split_pipe = line.split("\\|");
                split_tab = line.split("\\s{2,}");
                Double d = split_tab[2].matches("e-\\d+") ? Double.parseDouble("1" + split_tab[2]) : Double.parseDouble(split_tab[2]);
                list.add(new Match_Object(split_pipe[1], split_pipe[0], d, Integer.parseInt(split_tab[1]), round));
            }
            else if(line.matches("Results from round.+")){
                round ++;
            }
        }
        return list;
    }
    
    public static void main(String[] args) throws IOException {
        //new NR_BLAST_processor().read_BLAST_file(args[0]);
        new NR_BLAST_processor().read_NR_File("/home/proj/biosoft/PROTEINS/NR/nrdump.fasta");//args[0]
    }
}
