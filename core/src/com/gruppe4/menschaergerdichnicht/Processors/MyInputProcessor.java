package com.gruppe4.menschaergerdichnicht.Processors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gruppe4.menschaergerdichnicht.MainScreen;

/**
 * Created by manfrededer on 22.04.16.
 */
public class MyInputProcessor implements GestureDetector.GestureListener {
    private MainScreen myScreen;
    private Vector3 newPos = new Vector3(0,0,0);
    private Vector3 oldPos = new Vector3(0,0,0);
    private Vector3 delta = new Vector3(0,0,0);

    public MyInputProcessor(MainScreen screen){
        this.myScreen = screen;
    }
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        oldPos = new Vector3(x,y,0);
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    //wisch bewegung
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        newPos = new Vector3(x,y,0);
        delta = newPos.cpy().sub(oldPos);
        myScreen.getCamera().translate(-deltaX,deltaY);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        float disOld = initialPointer1.dst2(initialPointer2);
        float distNew = pointer1.dst2(pointer2);
        float dist = disOld-distNew;
        dist = dist / myScreen.getCamera().zoom;

        while(Math.abs(dist)>0.05){
            dist = dist / 5;
        }

        if(myScreen.getCamera().zoom+dist<1) {
            myScreen.getCamera().zoom = 1;
        } else if(myScreen.getCamera().zoom+dist >4){
            myScreen.getCamera().zoom = 4;
        } else {
            myScreen.getCamera().zoom += dist;
        }
        return false;
    }
}
