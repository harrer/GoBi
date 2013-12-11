package a;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Test {

    public static void main(String[] args) throws IOException{
        String[] split = "asd: ".split(":");
        for (String string : split) {
            System.out.println(string);
        }
    }
    
}
