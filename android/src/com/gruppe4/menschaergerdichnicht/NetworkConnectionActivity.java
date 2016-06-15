package com.gruppe4.menschaergerdichnicht;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.gruppe4.Logic.*;
import com.gruppe4.Logic.Player;
import com.gruppe4.menschaergerdichnicht.Logic.Draw;
import com.gruppe4.menschaergerdichnicht.Interface.ILibGDXCallBack;
import com.gruppe4.menschaergerdichnicht.Interface.Message;
import com.gruppe4.menschaergerdichnicht.Interface.IAndroidCallBack;
import com.gruppe4.menschaergerdichnicht.Interface.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


/**
 * Created by manfrededer on 19.04.16.
 */
public abstract class NetworkConnectionActivity extends AndroidApplication implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener,
        IAndroidCallBack{

    private static final long TIMEOUT_DISCOVER = 1000L * 30L;
    private boolean mIsHost = false;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MÄDN";
    private AlertDialog mConnectionRequestDialog;
    private MyListDialog mMyListDialog;
    private String myName = null;
    private Game myGame;
    private ILibGDXCallBack myGameCallBack;
    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_ETHERNET};
    private String mRemoteHostEndpoint;
    private ArrayList<String> mRemotePeerEndpoints = new ArrayList<>();


    public void setMyName(String myName) {
        this.myName = myName;
    }
    public void setMyGame(Game myGame) {
        this.myGame = myGame;
    }
    public void setmIsHost(boolean isHost){
        this.mIsHost = isHost;
    }
    private boolean wuerfelAllowed = false;
    private MenschAergerDIchNicht myLibgdx;
    private boolean connected = false;


    @Override
    public void playerWon(){
        if(mIsHost){
            sendMessageToAllClients(Serializer.serialize(new Message(MessageType.Victory, myName + " won this game")));
        } else {
            sendMessageToHost(Serializer.serialize(new Message(MessageType.Victory, myName + " won this game")));
        }
    }

    @Override
    public void playerMoved(int pinId, String color,String type,int from, int to,int rollValue){
        Draw d = new Draw(pinId,color,type,from,to,rollValue);
        if(!mIsHost){
            sendMessageToHost(Serializer.serialize(new Message(MessageType.PlayerMoved, d)));
            sendMessageToHost(Serializer.serialize(new Message(MessageType.PlayerRoled, myName + " rolled " + rollValue)));
            if(rollValue!=6){
                sendMessageToHost(Serializer.serialize(new Message(MessageType.NextPlayer,"")));
            } else{
                reRoll();
            }
        } else {
            sendMessageToAllClients(Serializer.serialize(new Message(MessageType.PlayerMoved,d)));
            sendMessageToAllClients(Serializer.serialize(new Message(MessageType.SimpleStringToPrint, this.myName + " rolled " + rollValue)));
            if(rollValue!=6){
                infoNextPlayer();
            } else {
                reRoll();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void reRoll(){
        //Print something wirft hier iwie Exception keine Ahnung wieso
        printSomeThing("Roll again!!!");
        wuerfelAllowed = true;
    }

    @Override
    public void cantRoll(int rollValue,int rollTrys){
        String reRolltext = (rollValue == 6 ||  rollTrys!=0) ? " but can roll again" : "";
        if(mIsHost){
            sendMessageToAllClients(Serializer.serialize(new Message(MessageType.SimpleStringToPrint, this.myName + " couldn't move" + reRolltext)));
            if(rollValue == 6 ||  rollTrys!=0){
                reRoll();
            }else{
                infoNextPlayer();
            }
        } else {
            sendMessageToHost(Serializer.serialize(new Message(MessageType.PlayerRoled, myName+" couldn't move"+reRolltext)));
            if(rollValue == 6 ||  rollTrys!=0){
                reRoll();
            } else {
                sendMessageToHost(Serializer.serialize(new Message(MessageType.NextPlayer,"")));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        myLibgdx = new MenschAergerDIchNicht();
        myLibgdx.setMyAndroidCallBack(this);
        myGameCallBack = myLibgdx;
        initialize(myLibgdx, config);

        //Init Shaker
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onStop(){
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        myLibgdx.resume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        myLibgdx.pause();
        super.onPause();
    }

    public void connect(boolean mIsHost){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .addApi(Nearby.MESSAGES_API)
                .build();
        setmIsHost(mIsHost);
        mGoogleApiClient.connect();
    }

    public void disconnect(){
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        if( !isConnectedToNetwork() )
            return;

        if( mIsHost ) {
            sendMessageToAllClients(Serializer.serialize(new Message(MessageType.SimpleStringToPrint, "Host has disconnected")));
            Nearby.Connections.stopAdvertising(mGoogleApiClient);
            Nearby.Connections.stopAllEndpoints( mGoogleApiClient );
            mIsHost = false;
            mRemotePeerEndpoints.clear();
        } else {
            if( !connected) {
                Nearby.Connections.stopDiscovery( mGoogleApiClient,null );
                return;
            }
            Nearby.Connections.disconnectFromEndpoint( mGoogleApiClient, mRemoteHostEndpoint );
            mRemoteHostEndpoint = null;
        }

        this.connected = false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(mIsHost){
            connected = true;
            startAdvertising();
        } else {
            startDiscovery();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMessageReceived(String sender, byte[] bytes, boolean b) {
        Message message = (Message)Serializer.deserialize(bytes);
        if(message != null){
            handleMyMessage(message,sender);
        }
    }

    private void handleMyMessage(Message message,String sender) {
        if(message.getMessage()!=null){
            if(message.getInfo().compareTo(MessageType.SimpleStringToPrint)==0){
                printSomeThing(message.getMessage().toString());
            } else if(message.getInfo().compareTo(MessageType.NewPlayer)==0){
                /*
                TO-Do
                 */
            } else if(message.getInfo().compareTo(MessageType.GameWorld)==0){
                myGame = (Game)message.getMessage();
                for(int i=0;i<myGame.getAllPlayer().size();i++){
                    myGameCallBack.playerAdded(myGame.getAllPlayer().get(i).getPlayerColor());
                }
            } else if(message.getInfo().compareTo(MessageType.YourTurn)==0){
                yourTurn();
            } else if(message.getInfo().compareTo(MessageType.PlayerRoled)==0){
                printRollInfo(message.getMessage().toString(), sender);
            } else if(message.getInfo().compareTo(MessageType.YourColor)==0){
                myGameCallBack.setMyColor(message.getMessage().toString());
            } else if(message.getInfo().compareTo(MessageType.PlayerMoved)==0){
                Draw d = (Draw) message.getMessage();
                if(!mIsHost){
                    myGameCallBack.movePin(d);
                } else {
                    sendMessageToOtherClients(Serializer.serialize(message),sender);
                    myGameCallBack.movePin(d);
                }
            } else if(message.getInfo().compareTo(MessageType.NextPlayer)==0){
                infoNextPlayer();
            } else if(message.getInfo().compareTo(MessageType.Victory)==0){
                printSomeThing(message.getMessage().toString());
                if(mIsHost){
                    sendMessageToOtherClients(Serializer.serialize(message),sender);
                }
            }
        }

    }

    private void printRollInfo(String rollMessage,String sender){
        printSomeThing(rollMessage);
        sendMessageToOtherClients(Serializer.serialize(new Message(MessageType.SimpleStringToPrint,rollMessage)),sender);
    }

    private void infoNextPlayer(){
        Player p = myGame.getNextPlayerToRoll();
        if(p.isHost()){
            yourTurn();
        } else {
            sendMessageToClient(Serializer.serialize(new Message(MessageType.YourTurn, "Its your Turn")), p.getEndPointId());

        }
    }

    private void yourTurn(){
        printSomeThing("Its your turn");
        this.wuerfelAllowed = true;
    }
    private static String printText;
    private void printSomeThing(String x){
        printText = x;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NetworkConnectionActivity.this, printText, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDisconnected(String s) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionRequest(final String remoteEndpointId, String deviceId, final String endpointName,
                                    byte[] payload) {
        if( mIsHost && myGame.getMaxPlayerCount()-1>this.mRemotePeerEndpoints.size()) {
            debugLog("onConnectionRequest:" + remoteEndpointId + ":" + endpointName);

            mConnectionRequestDialog = new AlertDialog.Builder(this)
                    .setTitle("Connection Request")
                    .setMessage("Do you want to connect to " + endpointName + "?")
                    .setCancelable(false)
                    .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            byte[] payload = null;
                            Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, remoteEndpointId,
                                    payload, NetworkConnectionActivity.this)
                                    .setResultCallback(new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                            if (status.isSuccess()) {
                                                debugLog("acceptConnectionRequest: SUCCESS");

                                                if (!mRemotePeerEndpoints.contains(remoteEndpointId)) {
                                                    mRemotePeerEndpoints.add(remoteEndpointId);
                                                    Player p = myGame.addPlayer(endpointName, remoteEndpointId);
                                                    myGameCallBack.playerAdded(p.getPlayerColor());
                                                    sendMessageToClient(Serializer.serialize(new Message(MessageType.YourColor, p.getPlayerColor())), remoteEndpointId);
                                                    sendMessageToOtherClients(Serializer.serialize(new Message(MessageType.SimpleStringToPrint, endpointName + " joined the game")), remoteEndpointId);
                                                    printSomeThing(endpointName + R.string.joinedTheGame);
                                                    sendMessageToAllClients(Serializer.serialize(new Message(MessageType.GameWorld, myGame)));

                                                    if(myGame.isFull()){
                                                        Player nextPlayerToRoll = myGame.getNextPlayerToRoll();
                                                        if(nextPlayerToRoll.isHost()){
                                                            yourTurn();
                                                        } else{
                                                            sendMessageToClient(Serializer.serialize(new Message(MessageType.YourTurn, "Its your Turn")),nextPlayerToRoll.getEndPointId());
                                                        }
                                                    }
                                                }
                                            } else {
                                                debugLog("acceptConnectionRequest: FAILURE");
                                            }
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, remoteEndpointId);
                        }
                    }).create();
        }else{
            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, remoteEndpointId);
        }

        mConnectionRequestDialog.show();
    }


    private void connectTo(String endpointId, final String endpointName) {
        debugLog("connectTo:" + endpointId + ":" + endpointName);

        // Send a connection request to a remote endpoint. By passing 'null' for the name,
        // the Nearby Connections API will construct a default name based on device model
        // such as 'LGE Nexus 5'.
        byte[] myPayload = null;
        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, myName, endpointId, myPayload,
                new Connections.ConnectionResponseCallback() {
                    @Override
                    public void onConnectionResponse(String endpointId, Status status,
                                                     byte[] bytes) {
                        Log.d(TAG, "onConnectionResponse:" + endpointId + ":" + status);
                        if (status.isSuccess()) {
                            debugLog("onConnectionResponse: " + endpointName + " SUCCESS");
                            connected = true;
                            Toast.makeText(NetworkConnectionActivity.this, "Connected to " + endpointName,
                                    Toast.LENGTH_SHORT).show();
                            mRemoteHostEndpoint = endpointId;
                        } else {
                            debugLog("onConnectionResponse: " + endpointName + " FAILURE");
                        }
                    }
                }, this);
    }

    public void startAdvertising() {
        try{
            if (!isConnectedToNetwork()) {
                debugLog("startAdvertising: not connected to WiFi network.");
                return;
            }

            // Identify that this device is the host
            mIsHost = true;

            // Advertising with an AppIdentifer lets other devices on the
            // network discover this application and prompt the user to
            // install the application.
            List<AppIdentifier> appIdentifierList = new ArrayList<>();
            appIdentifierList.add(new AppIdentifier(getPackageName()));
            AppMetadata appMetadata = new AppMetadata(appIdentifierList);

            // The advertising timeout is set to run indefinitely
            // Positive values represent timeout in milliseconds
            long NO_TIMEOUT = 0L;

            Nearby.Connections.startAdvertising(mGoogleApiClient, myName, appMetadata, NO_TIMEOUT,
                    this).setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
                @Override
                public void onResult(Connections.StartAdvertisingResult result) {
                    if (result.getStatus().isSuccess()) {
                        debugLog("startAdvertising:onResult: SUCCESS");
                        myGameCallBack.playerAdded(myGame.getHost().getPlayerColor());
                        myGameCallBack.setMyColor(myGame.getHost().getPlayerColor());

                    } else {
                        debugLog("startAdvertising:onResult: FAILURE ");
                        int statusCode = result.getStatus().getStatusCode();
                        if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_ADVERTISING) {
                            debugLog("STATUS_ALREADY_ADVERTISING");
                        } else {
                        }
                        // Advertising failed - see statusCode for more details
                    }
                }
            });
        }catch(Exception e){
            Log.e("ERROR",e.getMessage());
        }

    }

    public void sendMessageToHost(byte[] message){
        if(!mIsHost){
            Nearby.Connections.sendReliableMessage( mGoogleApiClient, mRemoteHostEndpoint, message);
        }
    }

    public void sendMessageToAllClients(byte[] message){
        if(mIsHost){
            Nearby.Connections.sendReliableMessage( mGoogleApiClient,mRemotePeerEndpoints, message);
        }
    }

    public void sendMessageToOtherClients(byte[] message, String sendingNotTo){
        ArrayList help = new ArrayList(mRemotePeerEndpoints);
        help.remove(sendingNotTo);
        if(mIsHost && help.size()!=0){
            Nearby.Connections.sendReliableMessage( mGoogleApiClient,help, message);
        }
    }

    public void sendMessageToClient(byte[] message,String toClient){
        if(mIsHost){
            Nearby.Connections.sendReliableMessage( mGoogleApiClient,toClient, message);
        }
    }

    private void debugLog(String msg) {
        Log.d(TAG, msg);
    }

    public void startDiscovery() {
        try{
            debugLog("startDiscovery");
            if (!isConnectedToNetwork()) {
                debugLog("startDiscovery: not connected to WiFi network.");
                return;
            }

            // Discover nearby apps that are advertising with the required service ID.
            String serviceId = getString(R.string.client_id);
            Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, TIMEOUT_DISCOVER, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                debugLog("startDiscovery:onResult: SUCCESS");
                            } else {
                                debugLog("startDiscovery:onResult: FAILURE");

                                // If the user hits 'Discover' multiple times in the timeout window,
                                // the error will be STATUS_ALREADY_DISCOVERING
                                int statusCode = status.getStatusCode();
                                if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_DISCOVERING) {
                                    debugLog("STATUS_ALREADY_DISCOVERING");
                                }
                            }
                        }
                    });
        } catch(Exception e){
            Log.e("ERROR",e.getMessage());
        }

    }



    private boolean isConnectedToNetwork() {
        ConnectivityManager connManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId,
                                final String endpointName) {
        Log.d(TAG, "onEndpointFound:" + endpointId + ":" + endpointName);

        // This device is discovering endpoints and has located an advertiser. Display a dialog to
        // the user asking if they want to connect, and send a connection request if they do.
        if (mMyListDialog == null) {
            // Configure the AlertDialog that the MyListDialog wraps
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Endpoint(s) Found")
                    .setCancelable(true)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMyListDialog.dismiss();
                        }
                    });

            // Create the MyListDialog with a listener
            mMyListDialog = new MyListDialog(this, builder, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String selectedEndpointName = mMyListDialog.getItemKey(which);
                    String selectedEndpointId = mMyListDialog.getItemValue(which);

                    NetworkConnectionActivity.this.connectTo(selectedEndpointId, selectedEndpointName);
                    mMyListDialog.dismiss();
                }
            });
        }

        mMyListDialog.addItem(endpointName, endpointId);
        mMyListDialog.show();
    }

    @Override
    public void onEndpointLost(String endpointId) {
        debugLog("onEndpointLost:" + endpointId);

        // An endpoint that was previously available for connection is no longer. It may have
        // stopped advertising, gone out of range, or lost connectivity. Dismiss any dialog that
        // was offering a connection.
        if (mMyListDialog != null) {
            mMyListDialog.removeItemByValue(endpointId);
        }
    }

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private long lastShake;
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 12 && wuerfelAllowed && lastShake+1000<System.currentTimeMillis()) {
                wuerfelAllowed = false;
                Random rand = new Random();
                int random = rand.nextInt(6)+1;
                printSomeThing("Device has shaken "+random);
                myGameCallBack.playerHasRoled(random);
                lastShake = System.currentTimeMillis();



            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
