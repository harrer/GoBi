package blatt6;

/**
 *
 * @author tobias
 */
public class GO_Term {
    
    private final String id;
    private final String[] children;

    public GO_Term(String id, String[] children){
        this.id = id;
        this.children = new String[children.length];
        System.arraycopy(children, 0, this.children, 0, children.length);
    }
    
    public String[] getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }
    
    
    
}
