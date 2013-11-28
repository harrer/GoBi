package a;

import java.util.ArrayList;

/**
 *
 * @author harrert
 */
public class Protein {
    
    private final ArrayList<Exon> exon;
    private final String proteinId;
    private final String chromosome;
    
    public Protein(Exon exon, String id, String chromosome) {
        this.exon = new ArrayList();
        this.exon.add(exon);
        proteinId = id;
        this.chromosome = chromosome;
    }

    public void addExon(Exon exon) {
        this.exon.add(exon);
    }

    public Exon getExon(int j) {
        return exon.get(j);
    }
    
    public ArrayList<Exon> getAllExons(){
        return exon;
    }

    public String getChromosome() {
        return chromosome;
    }

    public String getProteinId() {
        return proteinId;
    }
}
