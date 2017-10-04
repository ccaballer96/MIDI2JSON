/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi2json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 *
 * @author ccaba
 */
public class JWriter {

    private final List<Enemy> event_list;
    private final List<Marker> marker_list;
    private final Gson data;
    private final String out_path;
    private String output;

    public JWriter(List<Enemy> l, List<Marker> m, String str) {
        this.event_list = l;
        this.marker_list = m;
        Object[] aux = {l,m};
        this.data = new GsonBuilder().setPrettyPrinting().create();
        this.output = this.data.toJson(aux);
        this.out_path = str;
    }

    public void write() {
        try {
            File myFile = new File(out_path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(output);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
