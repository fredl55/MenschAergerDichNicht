package com.gruppe4.menschaergerdichnicht.Fields;

import java.awt.Color;

/**
 * Created by manfrededer on 03.05.16.
 */
public class HomeField extends Field {
    private String color;

    public HomeField(float x, float y, String name) {
        super(x, y, FieldType.HomeField,name);
        color = getColorFromName(name);

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

    public String getColor() {
        return color;
    }
}
