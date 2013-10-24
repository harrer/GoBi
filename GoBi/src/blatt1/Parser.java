package blatt1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    private String path = "/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/";

    public double[][] parseMatrix(String matrixName, boolean mirror) throws FileNotFoundException, IOException {
        String line;
        FileReader fReader = new FileReader(matrixName);
        BufferedReader reader = new BufferedReader(fReader);
        double[][] matrix = new double[20][20];
        int ln = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("MATRIX")) {
                String[] split = line.split("\\s");
                double[] d = new double[20];
                int column = 0;
                for (int i = 0; i < split.length; i++) {
                    if(split[i].matches("-?[0-9]+\\.[0-9]+")){
                        d[column] = Double.parseDouble(split[i]);
                        column++;
                    }
                }
                for (int i = column; i < 20; i++) {
                    d[i] = Double.NEGATIVE_INFINITY;
                }
                matrix[ln] = d;
                ln++;
            }
        }
        if (mirror) {
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    if (Double.compare(matrix[i][j], Double.NEGATIVE_INFINITY) == 0) {
                        matrix[i][j] = matrix[j][i];
                    }
                }
            }
        }
        reader.close();
        fReader.close();
        return matrix;
    }

    public HashMap<String, String> parseParams(String[] args) throws ParamException {
        HashMap<String, String> params = new HashMap<>();
        if (args.length < 4 || args.length / 2 == 1 || !args[0].equals("-pairs") || !args[2].equals("-seqlib")) {// # of params needs to be even
            throw new ParamException("less than 4/wrong params");
        } else {
            if (args[1].matches("(/[a-zA-Z_0-9.]+)+")) {
                params.put("-pairs", args[1]);
            } else {
                throw new ParamException("provide a valid path to the pairs file");
            }
            if (args[3].matches("(/[a-zA-Z_0-9.]+)+")) {
                params.put("-seqlib", args[3]);
            } else {
                throw new ParamException("provide a valid path to the seqlib file");
            }
            for (int i = 4; i < args.length; i++) {//store parameters
                switch (args[i]) {
                    case ("-m"):
                        if (i < args.length - 1 && args[i + 1].matches("BlakeCohenMatrix|dayhoff|THREADERSimilarityMatrix|blosum50|pam250")) {
                            params.put("-m", args[i + 1]);
                        } else {
                            throw new ParamException("Enter correct matrix name: BlakeCohenMatrix|dayhoff|THREADERSimilarityMatrix|blosum50|pam250");
                        }
                        break;
                    case ("-go"):
                        if (i < args.length - 1 && args[i + 1].matches("-?\\d+")) {
                            params.put("-go", args[i + 1]);
                        } else {
                            throw new ParamException("give a valid number");
                        }
                        break;
                    case ("-ge"):
                        if (i < args.length - 1 && args[i + 1].matches("-?\\d+")) {
                            params.put("-ge", args[i + 1]);
                        } else {
                            throw new ParamException("give a valid number");
                        }
                        break;
                    case ("-mode"):
                        if (i < args.length - 1 && args[i + 1].matches("local|global|freeshift")) {
                            params.put("-mode", args[i + 1]);
                        } else {
                            throw new ParamException("provide one of these modes: local|global|freeshift");
                        }
                        break;
                    case ("-printali"):
                        params.put("-printali", "true");
                        break;
                    case ("-printmatrices"):
                        if (i < args.length - 1 && args[i + 1].matches("txt|html")) {
                            params.put("-printmatrices", args[i + 1]);
                        } else {
                            throw new ParamException("format must be txt or html");
                        }
                        break;
                    case ("-check"):
                        params.put("-check", "true");
                        break;
                }
            }
        }
        return params;
    }

    public ArrayList<SeqPair> parsePairFile(String pairFile) throws FileNotFoundException, IOException {
        String line;
        ArrayList<SeqPair> list = new ArrayList<>();
        FileReader fReader = new FileReader(pairFile);
        BufferedReader reader = new BufferedReader(fReader);
        //String pattern = "(\\d+)\\s(\\d+)";
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] split = line.split("\\s");
            list.add(new SeqPair(split[0], split[1]));
        }
        reader.close();
        fReader.close();
        return list;
    }

    public HashMap<String, String> parseSeqlib(String seqlibFile) throws FileNotFoundException, IOException {
        String line;
        HashMap<String, String> map = new HashMap<>();
        FileReader fReader = new FileReader(seqlibFile);
        BufferedReader reader = new BufferedReader(fReader);
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] split = line.split(":");
            map.put(split[0], split[1]);
        }
        reader.close();
        fReader.close();
        return map;
    }
}
