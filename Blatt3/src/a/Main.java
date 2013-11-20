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

    HashMap<String, Gene> gene;

    public Main() {
        gene = new HashMap();
    }

    public void parse_file(String file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line, gene_id = "", transcript_id = "", exon_number = "";
        String[] split, split_id, id;
        while ((line = br.readLine()) != null) {//read lines
            split = line.split("\t");
            if (split[2].equals("CDS")) {//coding sequence
                split_id = split[8].split(";");
                id = split_id[0].split("\"");//get gene_id
                Gene g = null;
                if (!id[1].equals(gene_id)) {//new gene
                    g = new Gene(id[1], split[0], split[6]);
                    gene.put(id[1], g);
                    gene_id = id[1];
                }
                else{
                    id = split_id[1].split("\"");
                    Transcript transcript = null;
                    if(!id[1].equals(transcript_id)){
                        transcript_id = id[1];
                        transcript = new Transcript(new Protein(new Exon(split_id[2].split("\"")[1], Long.parseLong(split[4]), Long.parseLong(split[5])), split_id[5].split("\"")[1]));
                    }
                    else{
                        transcript.getProtein().addExon(new Exon(split_id[2].split("\"")[1], Long.parseLong(split[4]), Long.parseLong(split[5])));
                    }
                    g.addTranscript(transcript_id, transcript);
                    gene.put(transcript_id, g);
                }
            }
        }
        br.close();
        fr.close();
    }

    public void addGene(String geneId, Gene gene) {
        this.gene.put(geneId, gene);
    }

    public Gene get(String geneId) {
        return gene.get(geneId);
    }

    public static void main(String[] args) {

    }
}
