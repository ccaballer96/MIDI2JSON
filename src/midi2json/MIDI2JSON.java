/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi2json;

import java.io.File;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

/**
 *
 * @author ccaba
 */
public class MIDI2JSON {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            Sequence songFile = MidiSystem.getSequence(new File(args[0]));
            Song_Data data = new Song_Data(songFile);
            JWriter output = new JWriter(data.getEnemyList(), data.getMarkerList(), args[1]);
            output.write();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* List<Enemy> enemies = new ArrayList<Enemy>();
        enemies.add(new Enemy(0, Enemy.EnemyType.Flying));
        for (Enemy enemy : enemies) {
            try {
                System.out.println(enemy.enemyType.getCode());
            }catch(Exception e){
                    
                    }
            }
        } */
    }
}
