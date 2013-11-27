package a;

import java.util.ArrayList;

/**
 *
 * @author harrert
 */
public class Protein {
    
    ArrayList<Exon> exon;
    String proteinId;

    public Protein() {
        exon = new ArrayList();
    }
    
    public Protein(Exon exon, String id) {
        this.exon = new ArrayList();
        this.exon.add(exon);
    }
    
    public Protein(ArrayList<Exon> exon) {
        this.exon = exon;
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
}
