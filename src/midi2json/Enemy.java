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
public class Enemy {

    //private byte type;
    private final double time;
    private final double marker;
    private final EnemyType enemyType;

    public Enemy(int cod, double t, double m) throws Exception {
        time = t;
        marker = m;
        enemyType = this.getType(cod);
    }
    
    private EnemyType getType(int c) throws Exception{
        switch(c){
            case 40:    //E3
                return EnemyType.DOUBLE;
            case 43:    //G3
                return EnemyType.FLYING;
            case 36:    //C3
                return EnemyType.GROUND;
            case 48:    //C4
                return EnemyType.MAGE_2;
            case 50:    //D4
                return EnemyType.MAGE_3;
            case 52:    //E4
                return EnemyType.MAGE_4;
            case 53:    //F4
                return EnemyType.MAGE_5;
            case 55:    //G4
                return EnemyType.MAGE_6;
            case 57:    //A4
                return EnemyType.MAGE_7;
            case 59:    //B4
                return EnemyType.MAGE_8;
        }
        throw new Exception("Error: Code [" + c + "] is not defined");
    }

    private enum EnemyType {
        DOUBLE(-1), FLYING(1), GROUND(0), MAGE_2(2), MAGE_3(3), MAGE_4(4), MAGE_5(5), MAGE_6(6), MAGE_7(7), MAGE_8(8);
        private final int code;
        EnemyType(int c){
            this.code = c;
        }
        private int getCode(){
            return this.code;
        }
    }
    
    public EnemyType getType(){
        return this.enemyType;
    }
    
    public double getArrivalTime(){
        return this.time;
    }
    
    public double getMarkerTime(){
        return this.marker;
    }
    
    public boolean isMarker(){
        return this.marker != -1;
    }
    
    @Override
    public String toString(){
        return "Enemy Type: " + this.enemyType.name() + "\nCode: " + this.enemyType.getCode() + 
                "\nArrival Time: " + this.time + "\nMarker: " + Boolean.toString(this.marker != -1) +
                "\nMarker time: " + this.marker;
    }
}
