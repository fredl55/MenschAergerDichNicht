package com.gruppe4.menschaergerdichnicht;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gruppe4.Client.MyClient;
import com.gruppe4.interfaces.DataDisplay;
import com.gruppe4.server.MyServer;

public class ClientActivity extends Activity implements DataDisplay{
    MyClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        client = new MyClient();
    }

    @Override
    public void Display(String message) {
        TextView view = (TextView)findViewById(R.id.txtMessage);
        view.setText(message);
    }

    public void StartClient(View v){
        String ip = ((EditText)findViewById(R.id.txtIp)).getText().toString();
        if(!client.isConnected()){
            if(ip!=null && ip.equals("")==false){
                client.setIP(ip);
                client.setMessage("init");
                client.start();
            }
            else {
                ((TextView)findViewById(R.id.txtMessage)).setText("Please type in IP");
            }
        } else {
            ((TextView)findViewById(R.id.txtMessage)).setText("Client is already connected");
        }

    }

    public void SentMessage(View v){
        if(client.isConnected()){
            client.setMessage("Hallo");
        }
    }
}
