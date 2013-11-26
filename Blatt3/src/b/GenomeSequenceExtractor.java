package b;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author harrert
 */
public class GenomeSequenceExtractor {
    
    public String readExon(long start, long stop, String file) throws FileNotFoundException, IOException{
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] b = new byte[(int) (stop-start)];
        raf.seek(start);
        raf.read(b);
        return new String(b);
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String file = "/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_GENOME_FASTA/Homo_sapiens.GRCh37.63.dna.chromosome.Y.fa";
        String s = new GenomeSequenceExtractor().readExon(2, 5, "/tmp/test1347");
        System.out.println(s);
    }
}
