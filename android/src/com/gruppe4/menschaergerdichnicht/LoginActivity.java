package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void OnLogin(View v){
        String playerName = ((EditText)(findViewById(R.id.txtPlayerName))).getText().toString();
        Intent i = new Intent(this,MenuActivity.class);
        i.putExtra("PlayerName",playerName);
        startActivity(i);
    }

}
