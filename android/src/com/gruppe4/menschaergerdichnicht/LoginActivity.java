package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.gruppe4.Logic.Player;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    public void OnLogin(View v){
        EditText txtName = (EditText)findViewById(R.id.txtPlayerName);
        if( txtName.getText().toString().length() == 0 ) {
            txtName.setError(getString(R.string.error_empty));
        }
        else if(txtName.getText().toString().length() > 12 ) {
            txtName.setError(getString(R.string.error_tooLong));
        }
        else {
            String playerName = ((EditText) (findViewById(R.id.txtPlayerName))).getText().toString();
            Intent i = new Intent(this, MenuActivity.class);
            Player p = new Player(playerName);
            i.putExtra("Player", p);
            startActivity(i);
        }
    }

}
