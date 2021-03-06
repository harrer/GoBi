package abcd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author harrert
 */
public class Plot {

    public void map_Test(String path_in, String path_out, int round) throws IOException {
        StringBuilder sb = new StringBuilder();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path_out)));
        String tab = "\t", newline = "\n";
        FileReader fr = new FileReader(path_in);
        BufferedReader br = new BufferedReader(fr);
        HashMap<String, Double> map = new HashMap();
        String line;
        String[] split;
        while ((line = br.readLine()) != null) {
            split = line.split("\t");
            if (split.length == 7 && (Integer.parseInt(split[3]) <= round)) {
                if (map.containsKey(split[5])) {
                    if (Double.compare(Double.parseDouble(split[2]), map.get(split[5])) == -1) {
                        map.put(split[5], Double.parseDouble(split[2]));
                    }
                } else {
                    map.put(split[5], Double.parseDouble(split[2]));
                }
            }
        }

        HashMap<Double, Double> hMap = new HashMap();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            double e_value = entry.getValue();
            if (hMap.containsKey(e_value)) {
                hMap.put(e_value, hMap.get(e_value) + 1);
            } else {
                hMap.put(e_value, 1.0);
            }

        }

        HashMap<Double, Double> cMap = new HashMap();
        for (Map.Entry<Double, Double> entry : hMap.entrySet()) {
            cMap.put(entry.getKey(), 0.0);
        }

        for (Map.Entry<Double, Double> e : hMap.entrySet()) {
            double h = e.getKey();
            for (Map.Entry<Double, Double> entry : hMap.entrySet()) {
                double k = entry.getKey();
                double v = entry.getValue();
                if(Double.compare(k, h) <= 0){
                    cMap.put(h, cMap.get(h) + v);
                }
            }

        }

        for (Map.Entry<Double, Double> entry : cMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(tab);
            sb.append(entry.getValue());
            sb.append(newline);
        }
        writer.write(sb.toString());
        writer.close();
        fr.close();
        br.close();
    }

    public HashMap<String, Double> read_minimal_E_values(String path) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        HashMap<String, Double> map = new HashMap();
        String line;
        String[] split;
        while ((line = br.readLine()) != null) {
            split = line.split("\t");
            if (!split[1].equals("pir") && (split[3].equals("5"))) {
                double e_value = Double.parseDouble(split[2]);
                if (map.containsKey(split[0])) {
                    if (Double.compare(e_value, map.get(split[0])) == -1) {
                        map.put(split[0], e_value);
                    }
                } else {
                    map.put(split[0], e_value);
                }
            }
        }
        fr.close();
        fr.close();
        return map;
    }

    public HashMap<Double, Integer> map_E_values(HashMap<String, Double> e_values) {
        String line;
        String[] split;
        HashMap<Double, Integer> map = new HashMap();
        Iterator<Double> it = e_values.values().iterator();
        while (it.hasNext()) {
            double d = it.next();
            if (map.containsKey(d)) {
                map.put(d, map.get(d) + 1);
            } else {
                map.put(d, 1);
            }
        }
        return map;
    }

    public void put_map_to_file(HashMap<Double, Integer> map, String path) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
        String tab = "\t", newline = "\n";
        for (Map.Entry<Double, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append(tab);
            sb.append(entry.getValue());
            sb.append(newline);
        }
        writer.write(sb.toString());
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        Plot plot = new Plot();
//        HashMap<String, Double> min_map = plot.read_minimal_E_values("/home/proj/biocluster/praktikum/genprakt-ws13/abgaben/assignment2/harrer/2_d_mapping");
//        HashMap<Double, Integer> count_map = plot.map_E_values(min_map);
//        plot.put_map_to_file(count_map, "/home/h/harrert/Desktop/e-values");
        plot.map_Test("/home/proj/biocluster/praktikum/genprakt-ws13/abgaben/assignment2/harrer/2_e_enriched", "/home/h/harrert/Desktop/e-values/e-values_1", 1);
    }
}
