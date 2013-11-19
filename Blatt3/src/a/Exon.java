package a;

/**
 *
 * @author harrert
 */
public class Exon {
   
    private final String exon;
    private final long start;
    private final long stop;

    public Exon(String exon, long start, long stop) {
        this.exon = exon;
        this.start = start;
        this.stop = stop;
    }
    
    public String getExon() {
        return exon;
    }
}
