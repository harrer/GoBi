package a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Main {

    private final HashMap<String, Gene> gene;

    public Main() {
        gene = new HashMap();
    }

    public void parse_file(String file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line, gene_id = "", transcript_id = "", exon_number = "";
        String[] split, split_id, id;
        Gene g = null;
        Transcript transcript = null;
        while ((line = br.readLine()) != null) {//read lines
            split = line.split("\t");
            if (split[2].equals("CDS") && split[0].matches("\\d+|X|Y")) {//coding sequence and a chromosome 1-..., X,Y
                split_id = split[8].split(";");
                id = split_id[0].split("\"");//get gene_id
                if (!id[1].equals(gene_id)) {//new gene
                    g = new Gene(id[1], split[0], split[6]);
                    gene_id = id[1];
                }
                id = split_id[1].split("\"");
                if (!id[1].equals(transcript_id)) {//new Transcript
                    transcript_id = id[1];
                    transcript = new Transcript(new Protein(new Exon(split_id[2].split("\"")[1], Long.parseLong(split[3]), Long.parseLong(split[4])), split_id[5].split("\"")[1], split[0]));
                } else {
                    transcript.getProtein().addExon(new Exon(split_id[2].split("\"")[1], Long.parseLong(split[3]), Long.parseLong(split[4])));
                }
                g.addTranscript(transcript_id, transcript);
                gene.put(gene_id, g);
            }
        }
//        Gene ge = gene.get("ENSG00000215522");
//        Transcript tr = ge.getTranscript("ENST00000400492");
//        Exon ex = gene.get("ENSG00000215522").getTranscript("ENST00000400492").getProtein().getExon(6);
        br.close();
        fr.close();
    }

    public void addGene(String geneId, Gene gene) {
        this.gene.put(geneId, gene);
    }

    public Gene get(String geneId) {
        return gene.get(geneId);
    }

    public HashMap<String, Gene> getGene() {
        return gene;
    }
    
    

    public static void main(String[] args) throws IOException {
        Main m = new Main();
        String file = "/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.63.gtf";
        m.parse_file(file);
    }
}
