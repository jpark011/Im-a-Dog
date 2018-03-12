package com.cs446w18.a16.imadog.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import com.cs446w18.a16.imadog.commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by JayP on 2018-03-11.
 */

public class BluetoothServer extends Bluetooth {
    private static final int REQUEST_DISCOVERABLE_BT = 2222;

    private BluetoothServerSocket serverSocket;
    private HashMap<String, BluetoothSocket> clients;
    int clientCount; // TODO: Need to change to some kind of Player format

    public BluetoothServer(Context context) {
        super(context);
        serverSocket = null;
        clients = new HashMap();
        clientCount = 0;
    }

    public void open(Activity activity) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        activity.startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE_BT);
    }

    public void accept(String roomName, boolean insecureConnection) {
        new AcceptThread(roomName, insecureConnection);
    }

    @Override
    public void send(Command cmd) {
        for (BluetoothSocket client : clients.values()) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                send(cmd, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent){
        if(bluetoothCallback!=null){
            if(requestCode==REQUEST_DISCOVERABLE_BT){
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        if(resultCode != Activity.RESULT_CANCELED){
                            discoveryCallback.onDiscoverable();
                        } else {
                            discoveryCallback.onError("User denied");
                        }
                    }
                });
            }
        }
    }

    private class AcceptThread extends Thread {
        AcceptThread(String roomName, boolean insecureConnection) {
            try {
                if(insecureConnection) {
                    BluetoothServer.this.serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(roomName, uuid);
                } else {
                        BluetoothServer.this.serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(roomName, uuid);
                }
            } catch (IOException e) {
                if(communicationCallback!=null){
                    communicationCallback.onError(e.getMessage());
                }
            }
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                while (true) {
                    final BluetoothSocket client = serverSocket.accept();
                    final String clientName = "Player" + ++clientCount;
                    clients.put(clientName, client);

                    ObjectInputStream input = (ObjectInputStream)client.getInputStream();
                    new ReceiveThread(input);

                    if(communicationCallback!=null) {
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                communicationCallback.onAccept(clientName);
                            }
                        });
                    }
                }

            } catch (final IOException e) {
                if(communicationCallback!=null) {
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            communicationCallback.onConnectError(device, e.getMessage());
                        }
                    });
                }

                try {
                    socket.close();
                } catch (final IOException closeException) {
                    if (communicationCallback != null) {
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                communicationCallback.onError(closeException.getMessage());
                            }
                        });
                    }
                }
            }
        }
    }

    private class ReceiveThread extends Thread implements Runnable{
        private ObjectInputStream input;

        public ReceiveThread(ObjectInputStream input) {
            this.input = input;
        }

        public void run(){
            Command msg;
            try {
                while((msg = (Command)input.readObject()) != null) {
                    if(communicationCallback != null){
                        final Command msgCopy = msg;
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                communicationCallback.onMessage(msgCopy);
                            }
                        });
                    }
                }
            } catch (final IOException e) {
                connected=false;
                if(communicationCallback != null){
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            communicationCallback.onDisconnect(device, e.getMessage());
                        }
                    });
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
