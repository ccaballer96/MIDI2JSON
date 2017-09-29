/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi2json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

    private final List<Enemy> enem_list;
    private int bpm, tempo;
    private final int ppq;
    private final Sequence song;

    public Song_Data(Sequence seq) {
        song = seq;
        ppq = seq.getResolution();
        tempo = 500000; //default tempo
        bpm = 120;      //default BPM
        enem_list = makeEnemyList(song);
    }

    public List<Enemy> getEnemyList() {
        return enem_list;
    }

    public final int getPPQ() {
        return ppq;
    }

    public Sequence getSource() {
        return song;
    }

    // Returns a list of Enemy with Type, Arrival Time (in miliseconds) and Marker.
    private List<Enemy> makeEnemyList(Sequence s) {
        /*
        Create list for enemies and initialize variables. Because tempo events are stored in Track 1,
        beat patterns are stored there as well to reduce complexity.
         */
        List<Enemy> list = new ArrayList<>();
        Track[] tracks = s.getTracks();
        List<MidiEvent> listaOrdenada = mergeTracks(tracks[0], tracks[1]);
        Collections.sort(listaOrdenada, new Comparator<MidiEvent>() {
            @Override
            public int compare(MidiEvent e1, MidiEvent e2) {
                return (int) (e1.getTick() - e2.getTick());
            }
        });
        extractData(list, listaOrdenada);

        return list;
    }

    private List<MidiEvent> mergeTracks(Track a, Track b) {
        List<MidiEvent> res = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            res.add(a.get(i));
        }
        for (int j = 0; j < b.size(); j++) {
            res.add(b.get(j));
        }
        return res;
    }

    private void extractData(List<Enemy> el, List<MidiEvent> ml) {
        long delta_ticks, last_event_ticks = 0;
        long tick_us, delta_millis, time_millis = 0;
        boolean boss = false;
        for (MidiEvent event : ml) {
            if (event.getMessage() instanceof ShortMessage) {
                ShortMessage msg = (ShortMessage) event.getMessage();
                //Note events are stored in ShortMessages. Enemies' arrival time is based on NOTE_ON events timestamp.
                if (ShortMessage.NOTE_ON == msg.getCommand()) {
                    try {
                        if (msg.getData1() == 64) { //E5 to enable boss section
                            boss = true;
                        } else {
                            el.add(new Enemy(msg.getData1(), time_millis, -1, boss));
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
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
            last_event_ticks = event.getTick();
            delta_millis = (delta_ticks * tick_us) / 1000;
            time_millis += delta_millis;
        }
    }
}
