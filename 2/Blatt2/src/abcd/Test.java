package abcd;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author tobias
 */
public class Test {
    
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList();
        list.add("asd");
        list.add("#.ÄF2");
        list.add(".23f");
        list.add("21413132");
        list.add("f2löfm");
        Iterator<String> it = list.iterator();
        //while(it.hasNext()){
            System.out.println(it.next());
            System.out.println(it.next());
        //}
    }
}
