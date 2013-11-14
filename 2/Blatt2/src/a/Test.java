package a;

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
    
    private String[] split(String[] sa, String in, char split, StringBuilder sb){
        sb.delete(0, sb.toString().length());
        int c = 0;
        for (int i = 0; i < in.length(); i++) {
            if(in.charAt(i) == split){
                sa[c] = sb.toString();
                c++;
                sb.delete(0, sb.toString().length());
            }
            else{
                sb.append(in.charAt(i));
            }
        }
        sa[c] = sb.toString();
        sa[c+1] = ">>>##END##<<<";
        return sa;
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
