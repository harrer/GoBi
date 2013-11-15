package abcd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author tobias
 */
public class Test {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(System.getProperty("user.home") + "/Dropbox/UNI/a.txt");
        BufferedReader br = new BufferedReader(fr);
        fr.close();
        br.close();
    }
}
