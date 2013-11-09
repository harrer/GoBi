package b_c;

/**
 *
 * @author harrert
 */
public class NR_Object {
    
    public String sequence;
    private final String protein_ID;
    private final String source_DB;
    
    public NR_Object(){
        this.sequence = "";
        this.protein_ID = "";
        this.source_DB = "";
    }
    
    public NR_Object(String sequence, String protein_ID, String source_DB){
        this.sequence = sequence;
        this.protein_ID = protein_ID;
        this.source_DB = source_DB;
    }
    
    public String[] getVars(){
        return new String[] {sequence, protein_ID, source_DB};
    }
    
}
