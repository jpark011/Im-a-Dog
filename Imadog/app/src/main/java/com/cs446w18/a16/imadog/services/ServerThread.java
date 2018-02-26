package com.cs446w18.a16.imadog.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.controller.GameController;
import com.cs446w18.a16.imadog.controller.UserHost;
import com.cs446w18.a16.imadog.model.Room;

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

    // clients
    private ArrayList<UserHost> mClients;

    // Crated Room
    private Room mRoom;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTED = 2;  // now connected to a remote device

    public ServerThread(BluetoothAdapter btAdapter, String MY_UUID, Room room) {
        BluetoothServerSocket tmp = null;
        mClients = new ArrayList<>();
        mRoom = room;

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
        // Add myself (server as a client)
        UserHost server = new UserHost(null, Global.user, true);
        mClients.add(server);
        mRoom.addMember(server);

        // Listen to the server socket if we're not connected
        while (mState == STATE_LISTEN) {
            BluetoothSocket socket = null;

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                UserHost client = new UserHost(socket , null, false);
                mClients.add(client);
                mRoom.addMember(server);
            }
        }

        // Playing game
        while (mState == STATE_CONNECTED) {

        }

    }

    // used when ending gmae
    public void stopAccepting() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

