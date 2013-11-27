package bc;

/**
 *
 * @author harrert
 */
import a.*;
import java.io.IOException;
import java.util.ArrayList;

public class GenomicUtils {
    
    public static String[] findProteinSequence(Protein protein, String chromosome) throws IOException{
        ArrayList<Exon> exons = protein.getAllExons();
        String[] s = new String[exons.size()];
        int i=0;
        for (Exon exon : exons) {
            s[i] = translateExon(exon, chromosome);
            i++;
        }
        return s;
    }
    
    private static String translateExon(Exon exon, String chromosome) throws IOException{
        long start = exon.getStart();
        long stop = exon.getStop();
        String dna = GenomeSequenceExtractor.readExon(start, stop, chromosome);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dna.length()-3; i+=3) {
            sb.append(AminoAcidType.get(dna.substring(i, i+3)));
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        GenomeSequenceExtractor gse = new GenomeSequenceExtractor();
    }
}
