package bc;

/**
 *
 * @author harrert
 */
import a.*;
import java.util.ArrayList;

public class GenomicUtils {
    
    public static String[] findProteinSequence(Protein protein){
        ArrayList<Exon> exons = protein.getAllExons();
        String[] s = new String[exons.size()];
        int i=0;
        for (Exon exon : exons) {
            s[i] = translateExon(exon);
        }
        return s;
    }
    
    public static String translateExon(Exon exon){
        String dna = exon.getExon();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dna.length()-3; i+=3) {
            sb.append(AminoAcidType.get(dna.substring(i, i+3)));
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String dna = "CCCAAAGGGTAA";
        for (int i = 0; i <= dna.length()-3; i+=3) {
            sb.append(AminoAcidType.get(dna.substring(i, i+3)));
        }
        System.out.println(sb.toString());
    }
}
