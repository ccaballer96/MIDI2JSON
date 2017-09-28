/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi2json;

import java.util.ArrayList;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 *
 * @author ccaba
 */
public class Song_Data {

    private ArrayList<Enemy> enem_list;
    private int bpm, tempo;
    private final int ppq;
    private Sequence song;

    public Song_Data(Sequence seq) {
        song = seq;
        ppq = seq.getResolution();
        tempo = 500000; //default tempo
        bpm = 120;      //default BPM
        enem_list = makeEnemyList(song);
    }
    
    public ArrayList<Enemy> getEnemyList(){
        return enem_list;
    }
    
    public final int getPPQ(){
        return ppq;
    }
    
    public Sequence getSource(){
        return song;
    }

    // Returns a list of Enemy with Type, Arrival Time (in miliseconds) and Marker.
    private ArrayList<Enemy> makeEnemyList(Sequence s) {
        /*
        Create list for enemies and initialize variables. Because tempo events are stored in Track 1,
        beat patterns are stored there as well to reduce complexity.
         */
        ArrayList<Enemy> list = new ArrayList<>();
        long delta_ticks, last_event_ticks = 0;
        double tick_us, delta_millis, timeMillis = 0;
        Track track = s.getTracks()[0];

        for (int i = 0; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            if (event.getMessage() instanceof ShortMessage) {
                ShortMessage msg = (ShortMessage) event.getMessage();
                //Note events are stored in ShortMessages. Enemies' arrival time is based on NOTE_ON events timestamp.
                if (ShortMessage.NOTE_ON == msg.getCommand()) {
                    try {
                        list.add(new Enemy(msg.getData1(), timeMillis, -1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (event.getMessage() instanceof MetaMessage) {
                MetaMessage msg = (MetaMessage) event.getMessage();
                //SET_TEMPO code is 0x51 in hex
                if (msg.getType() == 0x51) {
                    //Tempo value is extracted from byte[] data and converted to int.
                    tempo = (((msg.getData()[0] & 0xFF) << 16) | ((msg.getData()[1] & 0xFF) << 8) | ((msg.getData()[2] & 0xFF)));
                    bpm = 60000000 / tempo;
                }
            }
            /*
            Compute time lapse between events with MIDI ticks, BPM and PPQ.
            Time is given in milliseconds.
             */
            tick_us = 60000000 / (bpm * ppq);
            delta_ticks = event.getTick() - last_event_ticks;
            last_event_ticks = event.getTick();;
            delta_millis = (delta_ticks * tick_us) / 1000;
            timeMillis += delta_millis;
        }
        return list;
    }
}
