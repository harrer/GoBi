package bc;

/**
 *
 * @author harrert
 */
import a.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenomicUtils {
    
    public static String getProteinSequence(Protein protein) throws IOException{
        ArrayList<Exon> exons = protein.getAllExons();
        StringBuilder sb = new StringBuilder();
        for (Exon exon : exons) {
            String ex = GenomeSequenceExtractor.readExon(exon.getStart(), exon.getStop(), protein.getChromosome());
            sb.append(ex);
        }
        return translateSequence(sb.toString());
    }
    
    private static String translateSequence(String sequence) throws IOException{
        StringBuilder sb = new StringBuilder();
        int start=0;
        while (!sequence.substring(start, start+3).equals("ATG")) {            
            start++;
        }
        for (int i = start; i < sequence.length()-3; i+=3) {
            sb.append(AminoAcidType.get(sequence.substring(i, i+3)));
            if(AminoAcidType.get(sequence.substring(i, i+3)) == 'X'){
                break;
            }
        }
        return sb.toString();
    }
    
    public static String translateGenome(HashMap<String, Gene> genes, String outFile){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            String geneId = entry.getKey();
            Gene gene = entry.getValue();
            for (Map.Entry<String, Transcript> transcript : gene.getTranscripts().entrySet()) {
                String transcriptId = transcript.getKey();
                Protein protein = transcript.getValue().getProtein();
                sb.append(">").append(protein.getProteinId()).append(" ");
                sb.append("chromosome:").append(protein.getChromosome());
            }
            
        }
    }
    
    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.parse_file("/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.63.gtf");
        HashMap<String, Gene> geneMap = m.getGene();
        Protein protein = geneMap.get("ENSG00000089163").getTranscript("ENST00000202967").getProtein();
        System.out.println(getProteinSequence(protein));
    }
}
