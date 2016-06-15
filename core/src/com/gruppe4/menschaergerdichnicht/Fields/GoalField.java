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
}
