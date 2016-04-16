package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.gruppe4.Helper.MyNetworkHelper;
import com.gruppe4.Logic.Game;
import com.gruppe4.Logic.Player;
import com.gruppe4.interfaces.DataDisplay;
import com.gruppe4.server.MyServer;

public class ServerActivity extends Activity implements DataDisplay{
    private MyServer server;
    private Game myGame;
    public static final int SERVERPORT = 8080;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        TextView v = (TextView)findViewById(R.id.txtIp);
        v.setText(MyNetworkHelper.getLocalIpAddress());
        myGame = new Game();
        HandleIntent();
        server = new MyServer();

    }

    public void connect(View view){
        if(!server.isStarted()){
            server.setEventListener(this);
            server.startListening();
        }
    }

    @Override
    public void Display(String message) {
        TextView v = (TextView) findViewById(R.id.txtServerMessage);
        v.setText(message);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(server.isStarted()){
            server.stopServer();
        }


    }

    private void HandleIntent(){
        Intent intent = getIntent();
        if(intent.hasExtra("Host")){
            myGame.setHost((Player)intent.getSerializableExtra("Host"));
        }
        if(intent.hasExtra("PlayersCount")){
            myGame.setMaxPlayerCount(intent.getIntExtra("PlayersCount",0));
        }
    }
}
