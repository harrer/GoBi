package blatt6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author tobias
 */
public class GO_parser {

    private static HashMap<String, GO_Term> hash;
    private static ArrayList<GO_Term> ontology;
    private static ArrayList<GO_Term> children;

    public GO_parser(String goFile) throws IOException {
        hash = new HashMap<>();
        ontology = goParser(goFile);
        children = new ArrayList<>();
    }

    public static ArrayList<GO_Term> goParser(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<GO_Term> list = new ArrayList<>();
        String line;
        ArrayList<String> goTerms = new ArrayList<>();
        while ((line = br.readLine()) != null && !line.contains("<term>")) {
        }
        while ((line = br.readLine()) != null) {
            if (line.contains("<id>") || line.contains("<is_a>")) {
                goTerms.add(line.split("[<>]")[2]);
            } else if (line.contains("</term>")) {
                String[] term = goTerms.toArray(new String[]{});
                String[] children = new String[term.length - 1];
                System.arraycopy(term, 1, children, 0, term.length - 1);
                hash.put(term[0], new GO_Term(term[0], children));
                list.add(new GO_Term(term[0], children));
                goTerms = new ArrayList<>();
            }
        }
        return list;
    }

    public static GO_Term findChildren(GO_Term term) {
        for (GO_Term t : ontology) {
            for (String s : t.getChildren()) {
                if (s.equalsIgnoreCase(term.getId())) {
                    children.add(t);
                    children.add(findChildren(t));
                }
            }
        }
        return null;
    }

    public static GO_Term getGOTerm(String id) {
        return hash.get(id);
    }

    public static void main(String[] args) throws IOException {
        long before = new Date().getTime();
        GO_parser parser = new GO_parser("/home/tobias/Desktop/go_daily-termdb.obo-xml");
        findChildren(getGOTerm("GO:0016301"));
        while (children.remove(null)) {
        }
        System.out.println("children:");
        HashMap<String, Boolean> map = new HashMap<>();
        ArrayList<Integer> redundants = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            if (map.containsKey(children.get(i).getId())) {
                redundants.add(i);
            }
            map.put(children.get(i).getId(), Boolean.TRUE);
        }
        int c = 0;
        for (Integer i : redundants) {
            children.remove(i - c);
            c++;
        }
        for (GO_Term term : children) {
            System.out.println(term.getId());
        }
        System.out.println((new Date().getTime() - before) + " ms");
    }

}
