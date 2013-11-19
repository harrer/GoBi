package a;

import java.util.HashMap;

/**
 *
 * @author harrert
 */
public class Gene {
    
    HashMap<String, Transcript> transcript;
    private final String id;
    private final String chromosome;
    private final String strand;

    public Gene(String id, String chromosome, String strand) {
        transcript = new HashMap();
        this.id = id;
        this.chromosome = chromosome;
        this.strand = strand;
    }

    public void addTranscript(String transcriptId, Transcript transcript){
        this.transcript.put(transcriptId, transcript);
    }
    
    public Transcript getTranscript(String transcriptId) {
        return transcript.get(transcriptId);
    }
}
