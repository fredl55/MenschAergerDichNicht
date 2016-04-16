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
    private Player player;
    private int playersCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        Intent i = getIntent();
        if(i.hasExtra("Player")){
            player = (Player)i.getSerializableExtra("Player");
        }
    }

    public void StartGame(View v){
        if(CheckInputs()){
            Intent i = new Intent(this,ServerActivity.class);
            i.putExtra("PlayersCount",playersCount);
            i.putExtra("Host",player);
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
