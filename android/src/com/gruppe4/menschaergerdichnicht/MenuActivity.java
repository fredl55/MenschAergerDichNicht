package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.gruppe4.Logic.Player;

public class MenuActivity extends Activity {
    private Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent i = getIntent();
        if(i.hasExtra("PlayerName")){
            player = new Player(i.getStringExtra("PlayerName"));
            ((TextView)findViewById(R.id.txtWelcome)).setText(String.format("Welcome %s",player.getName()));
        }
    }

    public void OnClickJoinGame(View v){
        Intent i = new Intent(this,ClientActivity.class);
        i.putExtra("Player", player);
        startActivity(i);
    }

    public void OnClickCreateGame(View v){
        Intent i = new Intent(this,CreateGameActivity.class);
        i.putExtra("Player",player);
        startActivity(i);
    }

}
