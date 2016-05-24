package com.gruppe4.menschaergerdichnicht.Interface;

import java.io.Serializable;

/**
 * Created by manfrededer on 02.05.16.
 */
public class Message implements Serializable{
    private Object myMessage;
    private String type;

    public Object getMessage() {
        return myMessage;
    }

    public String getInfo() {
        return type;
    }

    public Message(String type, Object msg){
        this.type=type;
        this.myMessage = msg;
    }

}
