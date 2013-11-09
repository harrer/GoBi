package b;

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
public class NCBI_NR_processor {
    
    private void readFile(String file) throws FileNotFoundException, IOException{
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
            if(100.0*c/lines >= h){
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
        System.out.println("finished in "+(runTime/60000)+" min "+(runTime/60)+"s; "+runTime+" ms");
    }
    
    public static void main(String[] args) throws IOException {
        new NCBI_NR_processor().readFile(args[0]);//"/home/tobias/Dropbox/UNI/GoBi/Blatt 2/head"
    }
}
