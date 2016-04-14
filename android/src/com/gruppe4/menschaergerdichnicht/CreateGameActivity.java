package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class CreateGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
    }

    public void StartGame(View v){
        Intent i = new Intent(this,ServerActivity.class);
        startActivity(i);
    }

}
