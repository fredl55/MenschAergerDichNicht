package com.gruppe4.Logic;


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
    private ArrayList<String> notUsedColors;
    private int freePlayerCount = maxPlayerCount-1;
    private int nextPlayer = -1;
    private Player currentRollingPlayer;

    public Game(){
        fillColor();
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

    public void setHost(Player hostPlayer) {
        host = hostPlayer;
        host.setPlayerColor(this.getUnusedColor());
        allPlayer.add(0,host);

    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public Player addPlayer(String endpointName,String endPointId){
        Player p = new Player(endpointName,endPointId);
        p.setId(maxPlayerCount-freePlayerCount);
        freePlayerCount--;
        p.setPlayerColor(getUnusedColor());
        allClientPlayers.add(p);
        allPlayer.add(p);
        return p;
    }


    private void fillColor(){
        this.notUsedColors = new ArrayList<>();
        notUsedColors.add(0,"blue");
        notUsedColors.add(1, "red");
        notUsedColors.add(2,"green");
        notUsedColors.add(3, "yellow");
    }

    private String getUnusedColor(){
        int zufallszahl = (int) Math.random()*notUsedColors.size();
        String color =  notUsedColors.get(zufallszahl);
        notUsedColors.remove(zufallszahl);
        return color;
    }

    public boolean isFull(){
        return this.allPlayer.size() == this.maxPlayerCount;
    }


    public Player getNextPlayerToRoll() {
        if(nextPlayer<maxPlayerCount-1){
            nextPlayer++;
        } else{
            nextPlayer=0;
        }
        currentRollingPlayer = this.getAllPlayer().get(nextPlayer);
        return currentRollingPlayer;

    }
}
