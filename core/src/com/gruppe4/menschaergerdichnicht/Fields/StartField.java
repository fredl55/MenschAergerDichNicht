package com.gruppe4.menschaergerdichnicht.Fields;

import java.awt.Color;

/**
 * Created by manfrededer on 03.05.16.
 */
public class StartField extends Field {
    private Color color;

    public StartField(float x, float y, String name) {
        super(x, y,FieldType.StartField,name);
        this.color = getColorFromName(name);
    }

    public Color getColor() {
        return color;
    }

    private Color getColorFromName(String name) {
        Color c = null;
        if(name.contains("Grün")){
            c = Color.GREEN;
        } else if(name.contains("Blau")){
            c = Color.BLUE;
        } else if(name.contains("Gelb")){
            c = Color.YELLOW;
        } else if(name.contains("Rot")){
            c = Color.RED;
        }
        return c;
    }
}
