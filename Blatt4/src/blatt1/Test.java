package blatt1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Test {

    public static void main(String[] args) throws IOException {
//        System.out.println(new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("exec /home/proj/biosoft/PROTEINS/software/TMalign ../PDB_REP_CHAINS/STRUCTURES/1bj4.A.pdb ../PDB_REP_CHAINS/STRUCTURES/2dkj.A.pdb").getInputStream())).readLine());
        BufferedReader br = new BufferedReader(new FileReader("/home/tobias/Desktop/mart_export.txt"));
        String line;
        ArrayList<String> all = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            all.add(line.split("\\s")[1]);
        }
        HashMap<String, Boolean> map = new HashMap<>();
        for (String string : all) {
            map.put(string, Boolean.TRUE);
        }
        System.out.println("");
    }
}
