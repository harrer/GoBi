package abcd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tobias
 */
public class Enrich_Ensembl {

    public HashMap<String, Object[]> read_mart_export() throws FileNotFoundException, IOException {
        HashMap<String, Object[]> map = new HashMap();
        FileReader fr = new FileReader("/home/h/harrert/Dropbox/UNI/GoBi/Blatt 2/mart_export_harrert.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        Pattern pattern = Pattern.compile("(ENSG\\d+)\\t(ENSP\\d+)\\t(NP_\\d+)");
        Matcher matcher;
        while ((line = br.readLine()) != null) {
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                map.put(matcher.group(3), new Object[]{matcher.group(1), matcher.group(2)});
            }
        }
        fr.close();
        br.close();
        return map;
    }

    public static void main(String[] args) throws IOException {
        new Enrich_Ensembl().read_mart_export();
    }
}
