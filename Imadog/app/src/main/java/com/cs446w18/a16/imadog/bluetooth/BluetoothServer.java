package com.cs446w18.a16.imadog.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.commands.SetClientNameCommand;
import com.cs446w18.a16.imadog.commands.UpdateLobbyCommand;
import com.cs446w18.a16.imadog.controller.GameController;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;
import com.cs446w18.a16.imadog.model.Chat;

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
    private HashMap<String, String> clientNames;
    private UserController hostUser;
    private CommunicationCallback serverCallback;
    private HashMap<String, CommunicationCallback> communicationCallbacks;
    int clientCount; // TODO: Need to change to some kind of Player format

    public BluetoothServer(Context context, UserController hostUser) {
        super(context);
        serverSocket = null;
        clients = new HashMap();
        clientNames = new HashMap();
        communicationCallbacks = new HashMap();
        clientCount = 0;
        serverCallback = new ServerCommunicationCallback();
        this.hostUser = hostUser;
        String username = hostUser.getUserName();
        ArrayList<String> list = new ArrayList<>();
        list.add(username);
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
            send(cmd, client);
        }
    }

    public void send(Command cmd, String clientName) {
        send(cmd, clients.get(clientName));
    }

    public void setCommunicationCallbacks(String clientName, CommunicationCallback cc) {
        this.communicationCallbacks.put(clientName, cc);
    }

    public ArrayList<String> getMembers() {
        ArrayList<String> names = new ArrayList<>(this.clientNames.values());
        names.add(hostUser.getUserName());
        hostUser.updateRoomMembers(names);
        return names;
    }

    public void setUserName(String clientName, String username) {
        this.clientNames.put(clientName, username);
        ArrayList<String> names = getMembers();
        Command cmd = new UpdateLobbyCommand(names);
        send(cmd);
    }

    public PlayerController startGame() {
        ArrayList<PlayerController> players = new ArrayList<>();
        PlayerController host = new PlayerController(null, null);
        host.setUserName(hostUser.getUserName());
        players.add(host);

        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            PlayerController player = new PlayerController(this, (String)pair.getKey());
            player.setUserName(clientNames.get(pair.getKey()));
            players.add(player);
        }

        initializeChat(players);
        GameController game = new GameController(players);
        hostUser.setGameController(game);
        return host;
    }

    private void initializeChat(ArrayList<PlayerController> users) {
        Chat chat = new Chat(new ArrayList<PlayerController>(users));
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setChat(chat);
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
                    ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                    clients.put(clientName, output);
                    communicationCallbacks.put(clientName, serverCallback);
                    Command cmd = new SetClientNameCommand(clientName);
                    send(cmd, output);

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

    private class ServerCommunicationCallback implements CommunicationCallback {

        public ServerCommunicationCallback() {
        }

        @Override
        public void onConnect(BluetoothDevice device) {

        }

        @Override
        public void onDisconnect(BluetoothDevice device, String message) {

        }

        @Override
        public void onMessage(Command command) {
            command.setReceiver(BluetoothServer.this);
            command.execute();
        }

        @Override
        public void onError(String message) {

        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {

        }

        @Override
        public void onAccept(String playerName) {

        }
    }
}
