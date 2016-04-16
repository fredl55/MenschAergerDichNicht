package com.gruppe4.Logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manfrededer on 15.04.16.
 */
public class Game {
    private Player host;
    private ArrayList<Player> clients = new ArrayList<Player>();
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

    public List<Player> getClients() {
        return clients;
    }

    public void setCheatingVariation(int cheatingVariation) {
        this.cheatingVariation = cheatingVariation;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void addClient(Player p){
        clients.add(p);
    }
}
