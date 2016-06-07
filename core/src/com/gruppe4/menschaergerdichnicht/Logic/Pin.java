package com.gruppe4.menschaergerdichnicht.Logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gruppe4.menschaergerdichnicht.MyAsstes;

import java.awt.Color;

import javax.xml.soap.Text;

/**
 * Created by manfrededer on 03.05.16.
 */
public class Pin {
    private String pinColor;
    private float posX;
    private float posY;
    private int number; // 1 - 4
    private Sprite myPin;
    private int fieldOffsetY = 85;

    public Pin(String c){
        this.pinColor = c;
        myPin = new Sprite(MyAsstes.assets.get(c+"Pin.png", Texture.class));
    }


    public void setPinColor(String pinColor) {
        this.pinColor = pinColor;
    }

    public void setPinPosition(float x, float y){
        this.posX = x;
        this.posY = y+fieldOffsetY;
        myPin.setPosition(posX,posY);
    }

    public String getPinColor() {
        return pinColor;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Sprite getMyPin() {
        return myPin;
    }
}
