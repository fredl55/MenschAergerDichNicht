package com.gruppe4.server;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gruppe4.Helper.MyNetworkHelper;
import com.gruppe4.interfaces.DataDisplay;
import com.gruppe4.menschaergerdichnicht.ServerActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by manfrededer on 14.04.16.
 */
public class MyServer {
    Thread myThread;
    ServerSocket serverSocket;

    DataDisplay myData;
    String serverIp;
    boolean started = false;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message status){
            myData.Display(status.obj.toString());
        }
    };


    public MyServer(){
        serverIp = MyNetworkHelper.getLocalIpAddress();
    }

    public void setEventListener(DataDisplay dataDisplay){
        myData = dataDisplay;
    }

    public boolean isStarted(){
        return started;
    }

    public void startListening(){
        /*myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    serverSocket = new ServerSocket(8080);
                    Message serverStatus =  Message.obtain();
                    serverStatus.obj = "server is listening at port 2001";
                    mHandler.sendMessage(serverStatus);
                    Socket connectedSocket = serverSocket.accept();
                    Message clientMessage = Message.obtain();
                    ObjectInputStream ois = new ObjectInputStream(connectedSocket.getInputStream());
                    String message = (String)ois.readObject();
                    clientMessage.obj = message;
                    mHandler.sendMessage(clientMessage);
                    ObjectOutputStream oos = new ObjectOutputStream(connectedSocket.getOutputStream());
                    oos.writeObject("Hey..!");
                    oos.close();
                    ois.close();
                    serverSocket.close();
                }
                catch(Exception exp){
                    Message exMessage = Message.obtain();
                    exMessage.obj = exp.getMessage();
                    mHandler.sendMessage(exMessage);
                }
            }
        });
        myThread.start();*/
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    if (serverIp != null) {
                        setServerMessage("Server listening on "+serverIp);
                        serverSocket = new ServerSocket(ServerActivity.SERVERPORT);
                        while (true) {
                            // LISTEN FOR INCOMING CLIENTS
                            Socket client = serverSocket.accept();
                            started = true;
                            setServerMessage("Connected");

                            try {
                                //BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                                //out.writeObject("Init OK");
                                out.flush();
                                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                                /*String line = null;
                                while ((line = in.readLine()) != null) {
                                    Log.d("ServerActivity", line);
                                }*/
                                Object o = null;
                                while((o=in.readObject())!=null){
                                    Log.d("Server",o.toString());
                                    if(o.toString().equals("Hallo")){
                                        out.writeObject("hallo ist schön");
                                    }
                                }
                                break;
                            } catch (Exception e) {
                                setServerMessage("Oops. Connection interrupted. Please reconnect your phones.");
                                e.printStackTrace();
                            }
                        }
                    } else {
                        setServerMessage("Couldn't detect internet connection.");
                    }
                } catch (Exception e) {
                    setServerMessage("Error "+e.getMessage());
                    e.printStackTrace();
                }

            }

            public void setServerMessage(String text){
                Message message = Message.obtain();
                message.obj = text;
                mHandler.sendMessage(message);

            }
        });
        myThread.start();

    }

    public void stopServer() {
        try {
            serverSocket.close();
            started = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
