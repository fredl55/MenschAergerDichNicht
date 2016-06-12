package com.gruppe4.menschaergerdichnicht.Logic;

import com.gruppe4.menschaergerdichnicht.Fields.FieldType;

import java.io.Serializable;

/**
 * Created by manfrededer on 11.06.16.
 */
public class Draw implements Serializable{
    private int pinId;
    private String fieldtype;
    private int to;
    private int from;
    private String color;
    private int rollValue;

    public Draw(int pinId, String color, String type,int from, int to, int rollValue){
        this.fieldtype = type;
        this.pinId = pinId;
        this.from = from;
        this.to = to;
        this.color = color;
        this.rollValue = rollValue;

    }
    public int getFrom() {
        return from;
    }

    public int getPinId() {
        return pinId;
    }

    public int getTo() {
        return to;
    }

    public String getFieldType() {
        return fieldtype;
    }

    public void setFieldType(String fieldType) {
        fieldtype = fieldType;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setPinId(int pinId) {
        this.pinId = pinId;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getRollValue() {
        return rollValue;
    }

    public void setRollValue(int rollValue) {
        this.rollValue = rollValue;
    }
}
