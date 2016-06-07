package com.gruppe4.Logic;

/**
 * Created by manfrededer on 07.06.16.
 */
public class Pin {
    private String color;
    private int position;

    public Pin(String color,int position){
        this.color = color;
        this.position = position+1;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getColor() {
        return color;
    }
}
