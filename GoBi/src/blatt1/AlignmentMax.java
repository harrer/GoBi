package blatt1;

/**
 *
 * @author tobias
 */
public class AlignmentMax {
    
    private int i,j;
    private double max;
    private final String mode;
    
    public AlignmentMax(int i, int j, double max, String mode){
        this.i = i;
        this.j = j;
        this.max = max;
        this.mode = mode;
    }
    
    public double getMax(){
        return max;
    }
    
    public void setMax(int i, int j, double max){
        this.i = i;
        this.j = j;
        this.max = max;
    }
}
