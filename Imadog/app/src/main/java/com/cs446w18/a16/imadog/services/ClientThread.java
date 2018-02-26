package com.cs446w18.a16.imadog.services;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.cs446w18.a16.imadog.controller.UserClient;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by JayP on 2018-02-25.
 */

public class ClientThread extends Thread {
    private final BluetoothSocket mSocket;
    private UserClient client;
    private  int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTED = 2;  // now connected to a remote device


    public ClientThread(BluetoothDevice device, String MY_UUID, UserClient client) {
        BluetoothSocket tmp = null;
        this.client = client;
        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSocket = tmp;
    }

    public void run() {
        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mSocket.connect();
        } catch (IOException e) {
            // Close the socket
            e.printStackTrace();
            try {
                mSocket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return;
        }

        // Playing the game
        while (client.isInGame()) {
            client.getInput();
        }
    }

    // Used when Game End
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
