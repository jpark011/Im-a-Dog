package com.cs446w18.a16.imadog.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.controller.GameController;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by JayP on 2018-03-11.
 */

public class BluetoothServer extends Bluetooth {
    private BluetoothServerSocket serverSocket;
    private HashMap<String, ObjectOutputStream> clients;
    private HashMap<String, CommunicationCallback> communicationCallbacks;
    int clientCount; // TODO: Need to change to some kind of Player format

    public BluetoothServer(Context context) {
        super(context);
        serverSocket = null;
        clients = new HashMap();
        communicationCallbacks = new HashMap();
        clientCount = 0;
    }

    public void open(Activity activity) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        activity.startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE_BT);
    }

    public void accept(String roomName, boolean insecureConnection) {
        new AcceptThread(roomName, insecureConnection).start();
    }

    @Override
    public void send(Command cmd) {
        for (ObjectOutputStream client : clients.values()) {
            send(cmd, out);
        }
    }

    public void setCommunicationCallbacks(String clientName, CommunicationCallback cc) {
        this.communicationCallbacks.put(clientName, cc);
    }

    public PlayerController startGame(UserController hostUser) {
        ArrayList<PlayerController> players = new ArrayList<>();
        PlayerController host = new PlayerController(this, null, null);
        players.add(host);

        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            PlayerController player = new PlayerController(this, (ObjectOutputStream)pair.getValue(), (String)pair.getKey());
            players.add(player);
        }

        GameController game = new GameController(players);
        host.setHost(game, hostUser);
        return host;
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
                    ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                    clients.put(clientName, output);

                    ObjectInputStream input = new ObjectInputStream(client.getInputStream());
                    new ReceiveThread(input, clientName).start();

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
        private String name;

        public ReceiveThread(ObjectInputStream input, String name) {
            this.name = name;
            this.input = input;
        }

        public void run(){
            Command msg;
            try {
                while((msg = (Command)input.readObject()) != null) {
                    if(communicationCallbacks.get(name) != null){
                        final Command msgCopy = msg;
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                communicationCallbacks.get(name).onMessage(msgCopy);
                            }
                        });
                    }
                }
            } catch (final IOException e) {
                connected=false;
                if(communicationCallbacks.get(name) != null){
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            communicationCallbacks.get(name).onDisconnect(device, e.getMessage());
                        }
                    });
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
