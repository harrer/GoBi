package bc;

/**
 *
 * @author harrert
 */
import a.*;
import d.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenomicUtils {

    public static String getProteinSequence(Protein protein, int headerOffset) throws IOException {
        ArrayList<Exon> exons = protein.getAllExons();
        StringBuilder sb = new StringBuilder();
        for (Exon exon : exons) {
            String ex = GenomeSequenceExtractor.readExon(exon.getStart(), exon.getStop(), protein.getChromosome(), headerOffset);
            sb.append(ex);
        }
        return translateSequence(sb.toString());
    }

    public static String translateSequence(String sequence) throws IOException {
        StringBuilder sb = new StringBuilder();
        int start = 0;
        while ((start + 3) < sequence.length() && !sequence.substring(start, start + 3).equals("ATG")) {
            start++;
        }
        for (int i = start; i < sequence.length() - 3; i += 3) {
            sb.append(AminoAcidType.get(sequence.substring(i, i + 3)));
            if (AminoAcidType.get(sequence.substring(i, i + 3)) == 'X') {
                break;
            }
        }
        return sb.toString();
    }

    public static HashMap<String, String> translateGenome(HashMap<String, Gene> genes, String path) throws IOException {
        HashMap<String, String> proteinSequences = new HashMap();
        HashMap<String, Integer> headerOffset = chromosomeHeaderOffset();
        StringBuilder sb = new StringBuilder();
        Double c = 0.0, p = 0.0;
        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            c++;
            if (c / genes.size() >= p / 100) {
                System.out.println(p.intValue() + "%");
                p++;
            }
            Gene gene = entry.getValue();
            for (Map.Entry<String, Transcript> transcript : gene.getTranscripts().entrySet()) {
                Protein protein = transcript.getValue().getProtein();
                String seq = getProteinSequence(protein, headerOffset.get(protein.getChromosome()));
                proteinSequences.put(protein.getProteinId(), seq);
                sb.append(protein.getProteinId()).append(":").append(seq).append("\n");
            }
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
        writer.write(sb.toString());
        return proteinSequences;
    }

    private static HashMap<String, Integer> chromosomeHeaderOffset() throws FileNotFoundException, IOException {
        String path = "/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_GENOME_FASTA/Homo_sapiens.GRCh37.63.dna.chromosome.";
        HashMap<String, Integer> files = new HashMap();
        FileReader fr;
        BufferedReader br;
        for (int i = 1; i <= 22; i++) {
            fr = new FileReader(path + i + ".fa");
            br = new BufferedReader(fr);
            files.put("" + i, br.readLine().length() + 1);
            br.close();
            fr.close();
        }
        fr = new FileReader(path + "X.fa");
        br = new BufferedReader(fr);
        files.put("X", br.readLine().length() + 1);
        br.close();
        fr.close();
        fr = new FileReader(path + "Y.fa");
        br = new BufferedReader(fr);
        files.put("Y", br.readLine().length() + 1);
        br.close();
        fr.close();
        return files;
    }

    private static HashMap<String, String> readSequences(String path) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        HashMap<String, String> map = new HashMap();
        String line;
        String[] split;
        while ((line = br.readLine()) != null) {
            split = line.split(":");
            
            if (split.length == 2) {
                map.put(split[0], split[1]);
            }
        }
        br.close();
        fr.close();
        return map;
    }

    private static HashMap<String, String> readReference(String path) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        HashMap<String, String> map = new HashMap();
        String line;
        String[] split;
        String id = "";
        while ((line = br.readLine()) != null) {
            if (line.startsWith(">")) {
                split = line.split(" ");
                id = split[0].substring(1);
                map.put(id, "");
            } else {
                map.put(id, (map.get(id) + line));
            }
        }
        br.close();
        fr.close();
        return map;
    }

    private static ArrayList<SeqPair> seqPairs(HashMap<String, String> prot, HashMap<String, String> ref) {
        ArrayList<SeqPair> list = new ArrayList();
        for (Map.Entry<String, String> entry : ref.entrySet()) {
            String refKey = entry.getKey();
            if (prot.containsKey(refKey)) {
                list.add(new SeqPair(refKey, refKey));
            }
        }
        return list;
    }

    public static void hashToFile(HashMap<? extends Object, ? extends Object> map, String path, char delimiter) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
            sb.append(entry.getKey().toString()).append(delimiter).append(entry.getValue().toString()).append("\n");
        }
        //System.out.println(sb.toString());
        Writer writer = new BufferedWriter(new FileWriter(path));
        writer.write(sb.toString());
    }
    
    private static HashMap<Double, Integer> identity(HashMap<String, Double> results){
        HashMap<Double, Integer> map = new HashMap();
        for (Map.Entry<String, Double> entry : results.entrySet()) {
            Double value = entry.getValue();
            if(map.containsKey(value)){
                map.put(value, map.get(value)+1);
            }
            else{
                map.put(value, 1);
            }
        }
        return map;
    }

    public static void main(String[] args) throws IOException, ParamException {
        Main m = new Main();
        m.parse_file("/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.63.gtf");
        HashMap<String, Gene> geneMap = m.getGene();
//        Protein protein = geneMap.get("ENSG00000089163").getTranscript("ENST00000202967").getProtein();
//        System.out.println(getProteinSequence(protein));
//        HashMap<String, String> translatedProteins = translateGenome(geneMap,"/tmp/translated_harrert_16_55");
        HashMap<String, String> translatedProteins = readSequences("/home/h/harrert/Dokumente/GoBi/proteins.seqlib");
        HashMap<String, String> refSeqs = readReference("/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_PEPTIDES/Homo_sapiens.GRCh37.63.pep.all.fa");
        ArrayList<SeqPair> list = seqPairs(translatedProteins, refSeqs);
        args = new String[]{"-pairs", "test.pairs", "-seqlib", "/home/h/harrert/Dokumente/GoBi/proteins.seqlib"};
        HashMap<String, Double> results = GoBi.runGotoh(args, list);
        HashMap<Double, Integer> identity = identity(results);
        hashToFile(identity, "/tmp/20_30_resultsH", ';');
    }
}
