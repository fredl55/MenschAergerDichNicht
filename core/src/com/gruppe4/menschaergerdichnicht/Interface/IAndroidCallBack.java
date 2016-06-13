package com.gruppe4.menschaergerdichnicht.Interface;

import com.gruppe4.menschaergerdichnicht.Fields.FieldType;

/**
 * Created by manfrededer on 01.05.16.
 */
public interface IAndroidCallBack {
    void playerMoved(int pinId, String color,String type,int from, int to, int rollvalue);
    void cantRoll(int rollValue,int rollTrys);
    void playerWon();

}
