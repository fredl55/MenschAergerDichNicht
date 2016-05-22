package com.gruppe4.menschaergerdichnicht.Logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.Color;

/**
 * Created by manfrededer on 03.05.16.
 */
public class Pin {
    private String pinColor;
    private float posX;
    private float posY;
    private int number; // 1 - 4
    private Sprite myPin;

    public Pin(String c){
        this.pinColor = c;
        if(pinColor == "blue"){
            myPin = new Sprite(new Texture("redPin.png"));
        } else if(pinColor == "red"){
            myPin = new Sprite(new Texture("redPin.png"));
        } else if(pinColor == "yellow"){
            myPin = new Sprite(new Texture("redPin.png"));
        } else if(pinColor == "green"){
            myPin = new Sprite(new Texture("redPin.png"));
        }
    }


    public void setPinColor(String pinColor) {
        this.pinColor = pinColor;
    }

    public void setPinPosition(float x, float y){
        this.posX = x;
        this.posY = y;
        myPin.setPosition(x,y);
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
