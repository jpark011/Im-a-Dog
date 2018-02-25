package com.cs446w18.a16.imadog.services;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by JayP on 2018-02-25.
 */

public class ClientThread extends Thread {
    private final BluetoothSocket mSocket;

    public ClientThread(BluetoothDevice device, String MY_UUID) {
        BluetoothSocket tmp = null;

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
