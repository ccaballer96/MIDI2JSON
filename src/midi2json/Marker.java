/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi2json;

/**
 *
 * @author ccaba
 */
public class Marker {
    
    private final String name;
    private final long time;
    private long dest_time;

    public long getDest_time() {
        return dest_time;
    }
    
    public Marker(String name, long time){
        this(name, time, -1);
    }

    public Marker(String name, long time, long dest_time) {
        this.name = name;
        this.time = time;
        this.dest_time = dest_time;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
    
    public void setDestTime(long dest_time){
        this.dest_time = dest_time;
    }
}
