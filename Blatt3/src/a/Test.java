package a;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author harrert
 */
public class Test {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        RandomAccessFile raf = new RandomAccessFile("/tmp/test1347", "r");
        byte[] b = new byte[stop-start];
        raf.seek(start);
        raf.read(b);
        System.out.println((char)b[0]);
    }
    
}
