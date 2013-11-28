package bc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author harrert
 */
public class GenomeSequenceExtractor {
    
    private static final String path = "/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_GENOME_FASTA/Homo_sapiens.GRCh37.63.dna.chromosome.";
    
    public static String readExon(long start, long stop, String chromosome, int headerOffset) throws FileNotFoundException, IOException{
        String file = path+chromosome+".fa";
        start += (headerOffset + (start/60));
        stop += (headerOffset + (stop/60));//skip the newline/linebreaks
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] b = new byte[(int) (stop-start+1)];
        raf.seek(start-1);
        raf.read(b);
        raf.close();
        String raw = new String(b);
        String[] sa = raw.split("\n");//remove the newlines/linebreaks
        StringBuilder sb = new StringBuilder();
        for (String string : sa) {
            sb.append(string);
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
    }
}
