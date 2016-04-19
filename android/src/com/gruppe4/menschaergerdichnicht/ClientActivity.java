package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
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
        //setContentView(R.layout.activity_client);
        super.connect(false);
    }
}
