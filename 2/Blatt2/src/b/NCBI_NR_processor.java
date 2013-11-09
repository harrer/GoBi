package b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author harrert
 */
public class NCBI_NR_processor {
    
    private static void readFile(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        NR_Object[] nr_objects;
        String line;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            line = br.readLine();
            System.out.println("asedfsdf");
            if(line.startsWith(">")){
                //sb.append(line).append("\n");
            }
            else{
                sb.append(line).append("\n");
            }
        }
        System.out.println(sb.toString());
    }
    
    public static void main(String[] args) throws IOException {
        readFile(args[0]);
    }
}
