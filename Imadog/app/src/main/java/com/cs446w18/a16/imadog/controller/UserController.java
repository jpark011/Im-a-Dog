package com.cs446w18.a16.imadog.controller;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.activities.menu.LobbyActivity;
import com.cs446w18.a16.imadog.bluetooth.Bluetooth;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.commands.SetUsernameCommand;
import com.cs446w18.a16.imadog.commands.SubmitAnswerCommand;
import com.cs446w18.a16.imadog.commands.VoteCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UserController {
    private String clientName;
    private String userName;
    private GameActivity view;
    private Bluetooth client;
    private BluetoothServer server;
    private boolean isServer;
    private PlayerController hostPlayer;
    private LobbyActivity lobby;
    private GameController gameController;

    public UserController(String name) {
        userName = name;
        view = null;
        client = null;
        server = null;
        clientName = null;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
        Command cmd = new SetUsernameCommand(clientName, userName);
        client.send(cmd);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public Bluetooth getClient() {
        return this.client;
    }

    public BluetoothServer getServer() {
        return this.server;
    }

    public void setClient(Bluetooth client) {
        this.isServer = false;
        this.client = client;
    }

    public void setServer(BluetoothServer server) {
        this.isServer = true;
        this.server = server;
    }

    public boolean isServer() {
        return this.isServer;
    }

    public void searchRoom(Activity activity) {
        this.client.startScanning(activity);
    }

    public void joinRoom(BluetoothDevice device) {
        this.client.connectToDevice(device, false);

    }

    public void leaveRoom() {}

    public void openRoom(Activity activity) {
        server.open(activity);
    }

    public ArrayList<String> getRoomMembers() {
        return server.getMembers();
    }

    public void updateRoomMembers(ArrayList<String> members) {
        lobby.updateLobbyMembers(members);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void createGame(String roomName) {
        this.server.accept(roomName, false);
    }

    public void startGame() {
        hostPlayer = this.server.startGame();
        gameController.readyToStart();
    }

    public void initializeGame(String question) {
        final String q = question;
        lobby.openGameActivity();
        if (!isServer) {
            client.setCommunicationCallback(new ClientCommunicationCallback());
        }
//        Command cmd = new SetUsernameCommand(userName);
//        sendCommand(cmd);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                readyForDay(q);
            }
        }, 5000);
    }

    public void setView(GameActivity view) {
        this.view = view;
    }

    public void setLobby(LobbyActivity lobby) {
        this.lobby = lobby;
    }

    public void submitAnswer(String answer) {
        Command cmd = new SubmitAnswerCommand(answer);
        sendCommand(cmd);
    }

    public void readyForDay(String question) {
        final String q = question;
        view.showDayPage();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                view.showQuestionPage(q);
            }
        }, 5000);
    }

    public void startPoll(String question, HashMap<String, String> answers) {
        view.showVotePage(question, answers);
    }

    public void closePoll(String name, String role, String winner) {
        final String result = winner;
        view.showVictimPage(name, role);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                if (result == null) {
                    view.showNightPage();
                } else {
                    view.showOutro(result);
                }
            }
        }, 5000);
    }

    public void startNightPoll(String title, ArrayList<String> names) {
        view.showNightVotePage(title, names);
    }

    public void closeNightPoll(String name,  String role, String winner, String question) {
        final String q = question;
        final String result = winner;
        view.showVictimPage(name, role);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                if (result == null) {
                    readyForDay(q);
                } else {
                    view.showOutro(result);
                }
            }
        }, 5000);
    }

    public void vote(String choice) {
        Command cmd = new VoteCommand(choice);
        sendCommand(cmd);
    }

    public void sendCommand(Command cmd) {
        if (isServer) {
            cmd.setReceiver(hostPlayer);
            cmd.execute();
        } else {
            client.send(cmd);
        }
    }

    private class ClientCommunicationCallback implements CommunicationCallback {
        @Override
        public void onConnect(BluetoothDevice device) {

        }

        @Override
        public void onDisconnect(BluetoothDevice device, String message) {

        }

        @Override
        public void onMessage(Command command) {
            command.setReceiver(Global.user);
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
