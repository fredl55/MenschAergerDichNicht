package com.gruppe4.menschaergerdichnicht.Fields;

import java.awt.Color;

/**
 * Created by manfrededer on 03.05.16.
 */
public class GoalField extends Field{
    private String color;



    public GoalField(float x, float y, String name) {
        super(x, y,FieldType.GoalField,name);
        this.color = getColorFromName(name);
    }

    public String getColor() {
        return color;
    }

    private String getColorFromName(String name) {
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
