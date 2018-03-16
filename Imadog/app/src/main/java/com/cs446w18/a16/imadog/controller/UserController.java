package com.cs446w18.a16.imadog.controller;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.activities.menu.LobbyActivity;
import com.cs446w18.a16.imadog.bluetooth.Bluetooth;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lacie on 2018-03-15.
 */

public class UserController {
    private String userName;
    private GameActivity view;
    private Bluetooth client;
    private BluetoothServer server;
    private boolean isServer;
    private PlayerController hostPlayer;
    private LobbyActivity lobby;

    public UserController(String name) {
        userName = name;
        view = null;
        client = null;
        server = null;
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
        if (!isServer) {
            client.setCommunicationCallback(new ClientCommunicationCallback());
        }
        this.client.connectToDevice(device, false);

    }

    public void leaveRoom() {}

    public void openRoom(Activity activity) {
        server.open(activity);
    }

    public void createGame(String roomName) {
        this.server.accept(roomName, false);
    }

    public void startGame() {
        hostPlayer = this.server.startGame(this);
        hostPlayer.readyToStart();
    }

    public void initializeGame(String question) {
        final String q = question;
        lobby.openGameActivity();

        sendCommand("SET_USERNAME", userName);
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
        sendCommand("SUBMIT_ANSWER", answer);
    }

    public void readyToStart() {
        if (isServer) {
            hostPlayer.readyToStart();
        }
    }

    public void readyForDay(String question) {
        final String q = question;
        view.showDayPage();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                view.showQuestionPage(q);
                if (isServer) {
                    hostPlayer.readyToAskQuestion();
                }
            }
        }, 5000);
    }

    public void readyForNight() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                if (isServer) {
                    hostPlayer.startNight();
                }
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
                    readyForNight();
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
        sendCommand("VOTE", choice);
    }

    public void endGame(String winner) {
        view.showOutro(winner);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                view.finishGame();
            }
        }, 5000);
    }

    public void notify(Command cmd) {
        String action = cmd.getAction();
        Object[] payload = cmd.getPayload();
        switch(action) {
            case "INITIALIZE_GAME":
                initializeGame((String)payload[0]);
                break;
            case "START_POLL":
                startPoll((String)payload[0], (HashMap<String, String>)payload[1]);
                break;
            case "CLOSE_POLL":
                closePoll((String)payload[0], (String)payload[1], (String)payload[2]);
                break;
            case "START_NIGHT_POLL":
                startNightPoll((String)payload[0], (ArrayList<String>)payload[1]);
                break;
            case "CLOSE_NIGHT_POLL":
                closeNightPoll((String)payload[0], (String)payload[1], (String)payload[2], (String)payload[3]);
                break;
            case "END_GAME":
                endGame((String)payload[0]);
                break;
        }
    }

    public void sendCommand(String action, Object... payload) {
        Command cmd = new Command(action, payload);
        if (isServer) {
            hostPlayer.notify(cmd);
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
            UserController.this.notify(command);
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
