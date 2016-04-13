package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void OnClickJoinGame(View v){
    }

    public void OnClickCreateGame(View v){
        Intent i = new Intent(this,CreateGameActivity.class);
        startActivity(i);
    }

}
