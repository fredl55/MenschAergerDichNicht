package com.gruppe4.menschaergerdichnicht.Processors;

import com.badlogic.gdx.Gdx;
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

        if(myScreen.isRolled()){
            myScreen.sendTap(x,y);
        }
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

        float cameraHalfWidth = myScreen.getCamera().viewportWidth * .5f;
        float cameraHalfHeight = myScreen.getCamera().viewportHeight * .5f;

        newPos = new Vector3(x,y,0);
        delta = newPos.cpy().sub(oldPos);

        float minX = cameraHalfWidth*myScreen.getCamera().zoom;
        float maxX = (myScreen.getMapWidth()- (cameraHalfWidth*myScreen.getCamera().zoom));
        float minY = cameraHalfWidth*myScreen.getCamera().zoom;
        float maxY = (myScreen.getMapHeight() -(cameraHalfHeight*myScreen.getCamera().zoom));
        float cameraLeftRight = (myScreen.getCamera().position.x - deltaX);
        float cameraTopBot = (myScreen.getCamera().position.y + deltaY);

        if(minX<=cameraLeftRight && cameraLeftRight <= maxX && minY <= cameraTopBot && cameraTopBot <= maxY){
            myScreen.getCamera().translate(-deltaX, deltaY);
            Gdx.app.log("GDX", "INSIDE!!!");
        }
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

        //workaround to forbid zooming out at the side from the map.
        if(dist>0){
            myScreen.getCamera().position.set(myScreen.getMapWidth()/2,myScreen.getMapHeight()/2,0);
        }
        while(Math.abs(dist)>0.05){
            dist = dist / 5;
        }

        if(myScreen.getCamera().zoom+dist<MainScreen.MINZOOM) {
            myScreen.getCamera().zoom = MainScreen.MINZOOM;
        } else if(myScreen.getCamera().zoom+dist >MainScreen.MAXZOOM){
            myScreen.getCamera().zoom =  MainScreen.MAXZOOM;
        } else {
            myScreen.getCamera().zoom += dist;
        }
        return false;
    }
}
