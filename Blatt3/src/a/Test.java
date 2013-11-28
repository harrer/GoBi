package a;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Test {

    public static void main(String[] args) throws IOException{
        HashMap<String, Double> map = new HashMap();
        map.put("asd", 1.0);
        map.put("ergewr", 0.7);
        map.put("3234", 0.9);
        bc.GenomicUtils.hashToFile(map, "/home/h/harrert/Downloads/21_20Test", ';');
    }
    
}
