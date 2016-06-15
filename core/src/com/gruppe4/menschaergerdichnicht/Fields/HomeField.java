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

    public String getColor() {
        return color;
    }
}
