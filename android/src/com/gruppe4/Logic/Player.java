package com.gruppe4.Logic;

import java.io.Serializable;

/**
 * Created by manfrededer on 15.04.16.
 */
public class Player implements Serializable{
    private String name;
    private String deviceName;

    public Player(String name){
        this.name = name;
    }

    public Player(String name,String deviceName){

        this.name = name;
        this.deviceName = deviceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
