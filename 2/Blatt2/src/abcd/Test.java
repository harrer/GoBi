package abcd;

import abcd.NR_Object;
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
 * @author tobias
 */
public class Test {
    
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("([^>]+)");
        String in = ">gi|66818355| >gi|sdad| asd< sda >gi|ffw|f2f ";
        Matcher matcher = pattern.matcher(in);
        while(matcher.find()){
            System.out.println(matcher.group(0));
        }
    }
    
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
                        //map.put(m1.group(1), new NR_Object("", m1.group(3), m1.group(2), index));
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
    
    public static void heapInfo(){
        int mb = 1024*1024;
         
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
         
        System.out.println("##### Heap utilization statistics [MB] #####");
         
        //Print used memory
        System.out.println("Used Memory:"
            + (runtime.totalMemory() - runtime.freeMemory()) / mb);
 
        //Print free memory
        System.out.println("Free Memory:"
            + runtime.freeMemory() / mb);
         
        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb);
 
        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb);
    }
}
