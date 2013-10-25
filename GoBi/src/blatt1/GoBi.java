package blatt1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author tobias
 */
public class GoBi {
    
    private static void matrix(String file) throws IOException, InterruptedException, ParamException{
        long timeBefore = new Date().getTime();
        final String path = "/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/matrices";
        double[][] m = new Parser().parseMatrix(path+file, false);
        String out = "";
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                //out += Double.compare(m[i][j], Double.NEGATIVE_INFINITY)==0? "- ":m[i][j] + " ";
                out += Double.compare(m[i][j], Double.NEGATIVE_INFINITY)==0? "-": m[i][j];
                out += "\t";
            }
            out += "\n";
        }
        System.out.println(out);
        long timeAfter = new Date().getTime();
        System.out.println("run: "+(timeAfter-timeBefore)+" ms");
    }
    
    private static void pairs(String file, boolean print) throws IOException{
        long timeBefore = new Date().getTime();
        final String path = "/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/";
        ArrayList<SeqPair> list = new Parser().parsePairFile(path+file);
        if (print) {
            StringBuilder sBuild = new StringBuilder();
            for (SeqPair pair : list) {
                sBuild.append(pair.getS1()).append(": ").append(pair.getS2()).append("\n");
            }
            System.out.println(sBuild.toString());
        }
        long timeAfter = new Date().getTime();
        System.out.println("run: "+(timeAfter-timeBefore)+" ms");
    }
    
    private static void seqLib(String file) throws IOException{
        long timeBefore = new Date().getTime();
        final String path = "/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/";
        HashMap<String, String> map = new Parser().parseSeqlib(path+file);
        System.out.println(map.get("11asB00"));
        long timeAfter = new Date().getTime();
        System.out.println("run: "+(timeAfter-timeBefore)+" ms");
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws blatt1.ParamException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, ParamException, InterruptedException  {
        //matrix("dayhoff.mat");
        //pairs("cathscop.outpairs", false);
        //seqLib("domains.seqlib");
        //above methods are TESTED!
        HashMap<String, String> map = new Parser().parseParams(args);
        System.out.println("done");
    }
}
