package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.TextView;

import com.gruppe4.Logic.Game;
import com.gruppe4.Logic.Player;

public class CreateGameActivity extends Activity {
    private Player hostPlayer;
    private int playersCount = 0;
    private RadioGroup playerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_create_game);
        Intent i = getIntent();
        if(i.hasExtra("Player")){
            hostPlayer = (Player)i.getSerializableExtra("Player");
        }
        playerCount = (RadioGroup) findViewById(R.id.playerCount);

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
        boolean retVal = true;
        //check if a radio button is selected
        if(playerCount.getCheckedRadioButtonId() == -1) {
            retVal = false;
        }
        //set the player count to selected value
        else{
            int id= playerCount.getCheckedRadioButtonId();
            View radioButton = playerCount.findViewById(id);
            int radioId = playerCount.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) playerCount.getChildAt(radioId);
            playersCount = Integer.parseInt(btn.getText().toString());
        }
        return retVal;
    }

}
