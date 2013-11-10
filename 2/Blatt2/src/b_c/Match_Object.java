package b_c;

/**
 *
 * @author tobias
 */
public class Match_Object {
    
    private final String protein_ID;
    private final String source_DB;
    private final double e_value;
    private final int score;
    private final int round;
    
    public Match_Object(String protein_ID, String source_DB, double e_value, int score, int round){
        this.protein_ID = protein_ID;
        this.source_DB = source_DB;
        this.e_value = e_value;
        this.score = score;
        this.round = round;
    }
    
    public Object[] getInfo(){
        return new Object[] {protein_ID, source_DB, e_value, round};
    }
}
