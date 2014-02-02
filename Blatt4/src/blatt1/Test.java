package blatt1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author harrert
 */
public class Test {

    public static void main(String[] args) throws IOException {
        System.out.println(new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("exec /home/proj/biosoft/PROTEINS/software/TMalign ../PDB_REP_CHAINS/STRUCTURES/1bj4.A.pdb ../PDB_REP_CHAINS/STRUCTURES/2dkj.A.pdb").getInputStream())).readLine());
    }

}
