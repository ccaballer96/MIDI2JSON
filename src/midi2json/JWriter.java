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

    private final List<Enemy> enemy_list;
    private final Gson data;
    private final String out_path;
    private final String output;

    public JWriter(List<Enemy> l, String str) {
        this.enemy_list = l;
        this.data = new GsonBuilder().setPrettyPrinting().create();
        this.output = this.data.toJson(l);
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
