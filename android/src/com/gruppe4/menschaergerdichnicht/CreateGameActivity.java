package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gruppe4.Logic.Game;
import com.gruppe4.Logic.Player;

public class CreateGameActivity extends Activity {
    private Player hostPlayer;
    private int playersCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_create_game);
        Intent i = getIntent();
        if(i.hasExtra("Player")){
            hostPlayer = (Player)i.getSerializableExtra("Player");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void StartGame(View v){
        if(CheckInputs()){
            Intent i = new Intent(this,ServerActivity.class);
            Game g = new Game();
            g.setMaxPlayerCount(playersCount);
            g.setHost(hostPlayer);
            i.putExtra("MyGame",g);
            startActivity(i);
        }

    }

    private boolean CheckInputs(){
        boolean retVal = false;
        EditText t = (EditText)findViewById(R.id.txtPlayersCount);
        playersCount = Integer.parseInt(t.getText().toString());
        if(t!= null && t.getText()!=null && playersCount >= 2){
            retVal=true;
        }
        return retVal;
    }

}
