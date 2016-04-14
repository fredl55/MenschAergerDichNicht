package com.gruppe4.Client;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gruppe4.interfaces.DataDisplay;
import com.gruppe4.menschaergerdichnicht.ServerActivity;

import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by manfrededer on 14.04.16.
 */
public class MyClient {
    Thread myClientThread;
    Socket clientSocket;
    DataDisplay myData;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message status){
            myData.Display(status.obj.toString());
        }
    };
    private String IP;
    private boolean connected=false;

    public void start(){
        myClientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    /*InetAddress serverAddr = InetAddress.getByName(IP);
                    clientSocket = new Socket(serverAddr,8080);
                    Log.d("ClientActivity", "connected: ");
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject("Hello There");
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    String msg = (String)ois.readObject();
                    Message serverMessage = Message.obtain();
                    serverMessage.obj = msg;
                    mHandler.sendMessage(serverMessage);
                    ois.close();
                    oos.close();*/

                    InetAddress serverAddr = InetAddress.getByName(IP);
                    Log.d("ClientActivity", "C: Connecting...");
                    Socket socket = new Socket(serverAddr, ServerActivity.SERVERPORT);
                    connected = true;
                    while (connected) {
                        try {
                            Log.d("ClientActivity", "C: Sending command.");
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            // WHERE YOU ISSUE THE COMMANDS
                            out.println("Hey Server!");
                            Log.d("ClientActivity", "C: Sent.");
                        } catch (Exception e) {
                            Log.e("ClientActivity", "S: Error", e);
                        }
                    }
                    socket.close();
                    Log.d("ClientActivity", "C: Closed.");

                }
                catch(Exception exp){
                    exp.printStackTrace();
                }
            }
        });
        myClientThread.start();
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public boolean isConnected(){
        return connected;
    }
}
