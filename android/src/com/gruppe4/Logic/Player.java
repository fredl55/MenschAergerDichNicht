package com.gruppe4.Logic;

import java.io.Serializable;

/**
 * Created by manfrededer on 15.04.16.
 */
public class Player implements Serializable{
    private String name;
    private String endPointId;

    public Player(String name){
        this.name = name;
    }

    public Player(String name,String endPointId){
        this.name = name;
        this.endPointId = endPointId;
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
}
