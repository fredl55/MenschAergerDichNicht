package com.gruppe4.menschaergerdichnicht.Interface;

import com.gruppe4.menschaergerdichnicht.Logic.Draw;

/**
 * Created by manfrededer on 23.05.16.
 */
public interface ILibGDXCallBack {
    public void playerAdded(String color);
    public void playerHasRoled(int number);
    public void setMyColor(String color);

    public void movePin(Draw draw);
}
