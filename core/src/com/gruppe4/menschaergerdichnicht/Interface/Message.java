package com.gruppe4.menschaergerdichnicht.Interface;

import java.io.Serializable;

/**
 * Created by manfrededer on 02.05.16.
 */
public class Message implements Serializable{
    private String stringMessage;
    private String type;

    public String getStringMessage() {
        return stringMessage;
    }

    public String getInfo() {
        return type;
    }

    public Message(String type, String msg){
        this.type=type;
        this.stringMessage = msg;
    }

}
