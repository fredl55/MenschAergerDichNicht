package com.gruppe4.Logic;

import java.io.Serializable;

/**
 * Created by manfrededer on 15.04.16.
 */
public class Player implements Serializable{
    private String name;

    public Player(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
