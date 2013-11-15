package abcd;

import java.util.ArrayList;

/**
 *
 * @author tobias
 */
public class Test {

    public static void main(String[] args) {
        ArrayList<Match_Object> list = new Combine().unserialize_al("/tmp/harrert_d_run1");
        System.out.println(list.get(12).toString());
    }
}
