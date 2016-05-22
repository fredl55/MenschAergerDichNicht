package com.gruppe4.Logic;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manfrededer on 15.04.16.
 */
public class Game implements Serializable{
    private Player host;
    private ArrayList<Player> allPlayer = new ArrayList<>();
    private ArrayList<Player> allClientPlayers = new ArrayList<>();
    private int maxPlayerCount;
    private int cheatingVariation;
    private ArrayList<Integer> notUsedColors;

    public Game(){
        fillColor();
    }

    public int getCheatingVariation() {
        return cheatingVariation;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public Player getHost() {
        return host;
    }

    public List<Player> getAllPlayer() {
        return allPlayer;
    }

    public void setCheatingVariation(int cheatingVariation) {
        this.cheatingVariation = cheatingVariation;
    }

    public void setHost(Player hostPlayer) {
        host = hostPlayer;
        host.setPlayerColor(this.getUnusedColor());
        allPlayer.add(0,host);

    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void addPlayer(Player p){
        p.setPlayerColor(getUnusedColor());
        allClientPlayers.add(p);
        allPlayer.add(p);
    }

    private void fillColor(){
        this.notUsedColors = new ArrayList<>();
        notUsedColors.add(0,Color.BLUE);
        notUsedColors.add(1, Color.RED);
        notUsedColors.add(2,Color.GREEN);
        notUsedColors.add(3,Color.YELLOW);
    }

    private int getUnusedColor(){
        int zufallszahl = (int) ((Math.random()*notUsedColors.size()));
        int color =  notUsedColors.get(zufallszahl);
        notUsedColors.remove(zufallszahl);
        return color;
    }

    private String getColorForPlayer(Player p){
        if(p.getPlayerColor() == 0){
            return "blue";
        } else if(p.getPlayerColor() == 1){
            return "red";
        }else if(p.getPlayerColor() == 2){
            return "green";
        } else {
            return "yellow";
        }
    }
}
