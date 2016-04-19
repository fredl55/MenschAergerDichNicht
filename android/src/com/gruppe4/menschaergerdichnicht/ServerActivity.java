package com.gruppe4.menschaergerdichnicht;

import android.os.Bundle;

import com.gruppe4.Logic.Game;

public class ServerActivity extends NetworkConnectionActivity{
    private Game myGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_server);
        super.connect(true);

    }

    @Override
    public void onStop(){
        super.onStop();
    }
}
