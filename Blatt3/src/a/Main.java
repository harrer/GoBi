package a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Main {
    
    HashMap<String, Gene> gene;

    public Main() {
        gene = new HashMap();
    }
    
    public void parse_file(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] split;
        while((line = br.readLine()) != null){
            split = line.split("\t");
            if(split[2].equals("CDS")){
                
            }
        }
        br.close();fr.close();
    }
    
    public void addGene(String geneId, Gene gene){
        this.gene.put(geneId, gene);
    }
    
    public Gene get(String geneId){
        return gene.get(geneId);
    }
    
    public static void main(String[] args) {
        
    }
}
