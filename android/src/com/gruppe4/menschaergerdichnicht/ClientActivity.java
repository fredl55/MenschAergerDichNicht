package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import com.gruppe4.Logic.Player;


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
}
