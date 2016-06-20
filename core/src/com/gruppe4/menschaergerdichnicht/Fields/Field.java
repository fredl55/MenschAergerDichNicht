package com.gruppe4.menschaergerdichnicht.Fields;


/**
 * Created by manfrededer on 03.05.16.
 */
public class Field {
    private float x;
    private float y;
    private String type;
    private int positionNr;

    public Field(float x, float y,String type,String name){
        this.x = x;
        this.y = y;
        this.type = type;
        positionNr = getPositionNrFromName(name);
    }

    private static int getPositionNrFromName(String name) {
        if(!name.isEmpty()){
            String help = "";
            for(int i=0; i <name.length();i++){
                if(Character.isDigit(name.charAt(i))){
                    help+=name.charAt(i);
                }
            }
            return Integer.parseInt(help);
        }
        return 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    public int getPositionNr() {
        return positionNr;
    }

    public String getColorFromName(String name) {
        String c = null;
        if(name.contains("Grün")){
            c = "green";
        } else if(name.contains("Blau")){
            c = "blue";
        } else if(name.contains("Gelb")){
            c = "yellow";
        } else if(name.contains("Rot")){
            c = "red";
        }
        return c;
    }
}
