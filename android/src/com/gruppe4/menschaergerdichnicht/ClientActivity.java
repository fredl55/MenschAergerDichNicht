package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.gruppe4.Logic.Player;
import com.gruppe4.Logic.Serializer;

import java.io.IOException;

public class ClientActivity extends NetworkConnectionActivity {
    Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        if(i.hasExtra("Player")){
            super.setMyName(((Player)i.getSerializableExtra("Player")).getName());
        }
        super.connect(false);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
