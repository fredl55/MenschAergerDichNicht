package com.gruppe4.Logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manfrededer on 15.04.16.
 */
public class Game implements Serializable{
    private Player host;
    private ArrayList<Player> allPlayer = new ArrayList<Player>();
    private int maxPlayerCount;
    private int cheatingVariation;

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
        allPlayer.add(0,host);
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void addPlayer(Player p){
        allPlayer.add(p);
    }
}
