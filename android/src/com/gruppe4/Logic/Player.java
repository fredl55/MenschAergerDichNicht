package com.gruppe4.Logic;
import android.graphics.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manfrededer on 15.04.16.
 */
public class Player implements Serializable{
    private int id;
    private String name;
    private String endPointId;
    private String playerColor;
    private boolean host = false;
    private ArrayList<Pin> myPins = new ArrayList<>();

    public Player(String name){
        this.name = name;
        this.host = true;
        this.id = 1;
        this.host=true;

    }


    public Player(String name,String endPointId){
        this.name = name;
        this.endPointId = endPointId;
        this.host = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(String endPointId) {
        this.endPointId = endPointId;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
        CreateStartingPins();
    }

    public void CreateStartingPins(){
        this.myPins.add(new Pin(this.playerColor,myPins.size()));
    }

    public boolean isHost() {
        return host;
    }
}
