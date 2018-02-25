package com.cs446w18.a16.imadog.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by JayP on 2018-02-25.
 */

public class ServerThread extends Thread {
    // The local server socket
    private final BluetoothServerSocket mmServerSocket;
    private int mState;

    // server socketc
    private BluetoothServerSocket mServer;

    // clients
    private ArrayList<BluetoothSocket> mClients;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTED = 2;  // now connected to a remote device

    public ServerThread(BluetoothAdapter btAdapter, String MY_UUID) {
        BluetoothServerSocket tmp = null;
        mClients = new ArrayList<>();

        // Create a new listening server socket
        try {
            tmp = btAdapter.listenUsingRfcommWithServiceRecord("I'm a dog", UUID.fromString(MY_UUID));
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

