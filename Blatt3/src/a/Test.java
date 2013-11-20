package a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author harrert
 */
public class Test {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.63.gtf");
        BufferedReader br = new BufferedReader(fr);
        String line, gene_id = "";
        String[] split, split_id, id;
        while((line = br.readLine()) != null){
            split = line.split("\t");
            split_id = split[8].split(";");
            id = split_id[0].split("\"");
            System.out.println(id[1]);
        }
        br.close();fr.close();
    }
    
}
