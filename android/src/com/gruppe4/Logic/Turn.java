package com.gruppe4.Logic;

/**
 * Created by manfrededer on 07.06.16.
 */
public class Turn {
    private int startPos;
    private int wuerfelCount;
    private int endPos;
    private boolean cheated;

    public Turn(int startPos,int wuerfelCount, int endPos){
        this.cheated = startPos+wuerfelCount != endPos;
        this.endPos = endPos;
        this.startPos = startPos;
        this.wuerfelCount = wuerfelCount;
    }

    public int getEndPos() {
        return endPos;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getWuerfelCount() {
        return wuerfelCount;
    }

    public boolean isCheated() {
        return cheated;
    }
}

