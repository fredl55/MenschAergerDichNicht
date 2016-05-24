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
import com.gruppe4.menschaergerdichnicht.Interface.ILibGDXCallBack;
import com.gruppe4.menschaergerdichnicht.Interface.Message;
import com.gruppe4.menschaergerdichnicht.Interface.IAndroidCallBack;
import com.gruppe4.menschaergerdichnicht.Interface.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
    private TextView mytextView;
    private MyListDialog mMyListDialog;
    private String myName = null;
    private Game myGame;
    private ILibGDXCallBack myGameCallBack;
    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_ETHERNET};
    private String mRemoteHostEndpoint;
    private ArrayList<String> mRemotePeerEndpoints = new ArrayList<String>();


    public void setMyName(String myName) {
        this.myName = myName;
    }
    public void setMyGame(Game myGame) {
        this.myGame = myGame;
    }
    public boolean ismIsHost(){
        return mIsHost;
    }
    public void setmIsHost(boolean isHost){
        this.mIsHost = isHost;
    }

    @Override
    public void onSendMessage(Message message) {
        sendMessageToHost(Serializer.serialize(message));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        MenschAergerDIchNicht myGame = new MenschAergerDIchNicht();
        myGame.setMyGameCallback(this);
        myGameCallBack = myGame;
        initialize(myGame, config);

        //Init Shaker
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
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
        if(mIsHost){
            mytextView = (TextView)findViewById(R.id.lblClientMessage);
        } else {
            mytextView = (TextView)findViewById(R.id.lblServerMessage);
        }
        mGoogleApiClient.connect();
    }

    public void disconnect(){
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(mIsHost){
            startAdvertising();
        } else {
            startDiscovery();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b) {
        Message message = (Message)Serializer.deserialize(bytes);
        if(message != null){
            handleMyMessage(message);
        }
    }

    private void handleMyMessage(Message message) {
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
            }
        }

    }

    private void printSomeThing(String x){
        Toast.makeText(NetworkConnectionActivity.this, x, Toast.LENGTH_SHORT).show();
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
                                                    Player p = new Player(endpointName, remoteEndpointId);
                                                    myGame.addPlayer(p);
                                                    myGameCallBack.playerAdded(p.getPlayerColor());
                                                    sendMessageToOtherClients(Serializer.serialize(new Message(MessageType.SimpleStringToPrint, endpointName + " joined the game")), remoteEndpointId);
                                                    printSomeThing(endpointName + " joined the game");
                                                    sendMessageToAllClients(Serializer.serialize(new Message(MessageType.GameWorld,myGame)));
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
        if(!mIsHost){
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
    private boolean alreadyShaken = false;

    private long lastShake = 0;
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 12 && lastShake+500<System.currentTimeMillis()) {
                Random rand = new Random();
                int random = rand.nextInt(6)+1;
                Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken."+random, Toast.LENGTH_SHORT);
                toast.show();
                lastShake = System.currentTimeMillis();
                myGameCallBack.playerHasRoled(random);

            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };



    /*@Override
    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        if (mAccel > 12 && !alreadyShaked) {
            alreadyShaked = true;
            Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG);
            toast.show();
            int zufallszahl = (int) ((Math.random() * 6) + 1);
            myGameCallBack.playerHasRoled(zufallszahl);

        }
    }*/

    /*let it shake */
    /*private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 12 && !alreadyShaked) {
                alreadyShaked = true;
                Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG);
                toast.show();
                int zufallszahl = (int) ((Math.random()*6)+1);

            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };*/




}
