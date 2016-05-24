package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import com.gruppe4.Logic.Player;

import com.gruppe4.Logic.Game;

public class ServerActivity extends NetworkConnectionActivity{
    private Game myGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        if(i.hasExtra("MyGame")){
            myGame = (Game)i.getSerializableExtra("MyGame");
            super.onCreate(savedInstanceState);
            super.setMyName(myGame.getHost().getName());
            super.setMyGame(myGame);
            super.connect(true);
        }


    }
}
