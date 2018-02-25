package com.cs446w18.a16.imadog.activities.menu;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class CreateGameActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */
    /**
     * Tag for Log
     */
    private static final String TAG = "JoinGameActivity";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;

    /**
     * Newly discovered devices
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private Thread mThread;

    // server socket
    private BluetoothServerSocket mServer;

    // clients
    private ArrayList<BluetoothSocket> mClients;

    private final String MY_UUID = getString(R.string.UUID);
    // Name field
    EditText nameField;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        mClients = new ArrayList<>();
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Create button is pressed
    public void createGame(View view) {
        // Make it discoverable for 10 mins
        if (mBtAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
            startActivity(discoverableIntent);
        }

        // Start Server Thread;
        mThread = new AcceptThread();

        // Ready to goto Lobby
        Intent createIntent = new Intent(CreateGameActivity.this, LobbyActivity.class);
        createIntent.putExtra("isHost", true);

        startActivity(createIntent);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private int mState;

        // Constants that indicate the current connection state
        public static final int STATE_NONE = 0;       // we're doing nothing
        public static final int STATE_LISTEN = 1;     // now listening for incoming connections
        public static final int STATE_CONNECTED = 2;  // now connected to a remote device

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                    tmp = mBtAdapter.listenUsingRfcommWithServiceRecord("I'm a dog", UUID.fromString(MY_UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmServerSocket = tmp;
            mState = STATE_LISTEN;
        }

        public void run() {
            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // If a connection was accepted
                if (socket != null) {
                    mClients.add(socket);
                    Log.d(TAG,"Clinets: " + mClients.size());
                }
            }

            // Playing game
            while (mState == STATE_CONNECTED) {

            }

        }

        // used when ending gmae
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
