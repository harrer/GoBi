package b_c;

/**
 *
 * @author harrert
 */
public class NR_Object {
    
    private final String sequnece;
    private final String protein_ID;
    private final String source_DB;
    private final int index;
    
    public NR_Object(){
        this.sequnece = "";
        this.protein_ID = "";
        this.source_DB = "";
        index = 0;
    }
    
    public NR_Object(String sequence, String protein_ID, String source_DB, int index){
        this.sequnece = sequence;
        this.protein_ID = protein_ID;
        this.source_DB = source_DB;
        this.index = index;
    }
    
    public Object[] getVars(){
        return new Object[] {sequnece, protein_ID, source_DB, index};
    }
    
}
