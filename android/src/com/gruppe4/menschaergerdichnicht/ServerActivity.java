package com.gruppe4.menschaergerdichnicht;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.gruppe4.Helper.MyNetworkHelper;
import com.gruppe4.interfaces.DataDisplay;
import com.gruppe4.server.MyServer;

public class ServerActivity extends Activity implements DataDisplay{
    private MyServer server;
    public static final int SERVERPORT = 8080;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        TextView v = (TextView)findViewById(R.id.txtIp);
        v.setText(MyNetworkHelper.getLocalIpAddress());
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
}
