package com.gruppe4.menschaergerdichnicht.Logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gruppe4.menschaergerdichnicht.Fields.Field;
import com.gruppe4.menschaergerdichnicht.Fields.FieldType;
import com.gruppe4.menschaergerdichnicht.MyAssets;

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
    private boolean selected;
    private String currentType;
    private int positionNr;
    private int oldPositionNr;

    public Pin(int number,String c){
        this.number = number;
        this.pinColor = c;
        myPin = new Sprite(MyAssets.assets.get(c+"Pin.png", Texture.class));
        currentType = FieldType.HomeField;
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


    public int getNumber() {
        return number;
    }

    public Sprite getMyPin() {
        return myPin;
    }


    public boolean isSelected() {
        return selected;
    }

    public void switchSelection(){
        if(isSelected()){
            selected = false;
            myPin.scale(-1.1f);
        } else {
            selected = true;
            myPin.scale(1.1f);
        }
    }

    public void switchSelectionOff(){
        if(selected){
            myPin.scale(-1.1f);
            selected = false;
        }
    }

    public String getCurrentType() {
        return currentType;
    }


    public int getPositionNr() {
        return positionNr;
    }

    public int getOldPositionNr() {
        return oldPositionNr;
    }

    public void setOldPositionNr(int oldPositionNr) {
        this.oldPositionNr = oldPositionNr;
    }

    public void setField(Field f){
        this.positionNr = f.getPositionNr();
        this.setPinPosition(f.getX(),f.getY());
        this.currentType = f.getType();
    }

    public float getPosX(){
        return this.posX;
    }

    public float getPosY(){
        return this.posY;
    }

}
