package blatt1;

/**
 *
 * @author tobias
 */
public class AlignmentMax {
    
    private int i,j;
    private int max;
    private final String mode;
    
    public AlignmentMax(int i, int j, int max, String mode){
        this.i = i;
        this.j = j;
        this.max = max;
        this.mode = mode;
    }
    
    public int getMax(){
        return max;
    }
    
    public void setMax(int i, int j, int max){
        this.i = i;
        this.j = j;
        this.max = max;
    }
}
