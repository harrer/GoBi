package abcd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tobias
 */
public class Enrich_Ensembl {

    public HashMap<String, String[]> read_mart_export() throws FileNotFoundException, IOException {
        HashMap<String, String[]> map = new HashMap();
        FileReader fr = new FileReader("/home/h/harrert/Dropbox/UNI/GoBi/Blatt 2/mart_export_harrert.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        Pattern pattern = Pattern.compile("(ENSG\\d+)\\t(ENSP\\d+)\\t(NP_\\d+)");
        Matcher matcher;
        while ((line = br.readLine()) != null) {
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                map.put(matcher.group(3), new String[]{matcher.group(1), matcher.group(2)});
            }
        }
        fr.close();
        br.close();
        return map;
    }
    
    public void enrich(ArrayList<ArrayList<Object>> list) throws IOException{
        HashMap<String, String[]> ens_map = read_mart_export();
        Pattern pattern = Pattern.compile("(NP_\\d+)(\\.\\d+)?");
        Matcher matcher;
        for (ArrayList<Object> l : list) {
            matcher = pattern.matcher(l.get(0).toString());
            while(matcher.find()){
                if(ens_map.containsKey(matcher.group(1))){
                    l.add(ens_map.get(matcher.group(1))[0]);
                    l.add(ens_map.get(matcher.group(1))[1]);
                    System.out.println(matcher.group(1)+" enriched");
                }
            }
        }
    }
}
