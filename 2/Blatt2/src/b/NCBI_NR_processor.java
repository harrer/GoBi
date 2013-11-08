package b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author harrert
 */
public class NCBI_NR_processor {
    
    private static void readFile(String file) throws FileNotFoundException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        NR_Object[] nr_objects;
        for (int i = 0; i < 200; i++) {
            
        }
    }
    
    public static void main(String[] args) {
        
    }
}
