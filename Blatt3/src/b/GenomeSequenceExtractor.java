package b;

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
    
    private final String path = "/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_GENOME_FASTA/Homo_sapiens.GRCh37.63.dna.chromosome.";
    
    public String readExon(long start, long stop, String chromosome) throws FileNotFoundException, IOException{
        String file = path+chromosome+".fa";
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();//open filehandle to check length of header line
        int header_offset = line.length() + 1;
        start += (header_offset + (start/60));
        stop += (header_offset + (stop/60));//skip the newline/linebreaks
        br.close(); fr.close();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
//        RandomAccessFile raf = new RandomAccessFile(chromosome, "r");
        byte[] b = new byte[(int) (stop-start+1)];
        raf.seek(start-1);
        raf.read(b);
        return new String(b);
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(new GenomeSequenceExtractor().readExon(59930, 60200, "X"));
    }
}
