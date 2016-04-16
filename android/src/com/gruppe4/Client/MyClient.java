package com.gruppe4.Client;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gruppe4.interfaces.DataDisplay;
import com.gruppe4.menschaergerdichnicht.ServerActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
    String message=null;
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
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    SendMessageToServer(out,in);
                    out.flush();

                    connected = true;
                    while (connected) {
                        try {
                            SendMessageToServer(out,in);


                        } catch (Exception e) {
                            Log.e("ClientActivity", "S: Error", e);
                        }
                    }
                    in.close();
                    out.close();
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

    private void HandleServerMessage(ObjectInputStream in) throws IOException, ClassNotFoundException {

        Object msg=null;
        if((msg=in.readObject())!=null){
            Log.d("ClientActivity", msg.toString());
        }
    }

    private void SendMessageToServer(ObjectOutputStream out,ObjectInputStream in) throws IOException, ClassNotFoundException {
        if(message != null){
            out.writeObject(message);
            out.flush();
            Log.d("Client", "Message was sent");
            message = null;
            HandleServerMessage(in);
        }
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public boolean isConnected(){
        return connected;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
