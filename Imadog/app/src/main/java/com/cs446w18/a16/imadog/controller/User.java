package com.cs446w18.a16.imadog.controller;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.bluetooth.Bluetooth;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.model.Player;
import com.cs446w18.a16.imadog.model.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lacie on 2018-02-18.
 */

public class User {
    private String userName;
    private Room room;
    private Player role;
    private GameActivity view;
    private GameController gameController;
    private Bluetooth client;   // if client
    private BluetoothServer server;   // if server
    private boolean isServer;

    public User(String name) {
        userName = name;
        room = null;
        role = null;
        view = null;
        gameController = null;
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

    public void searchRoom() {
        this.client.startScanning();
    }

    public void joinRoom(BluetoothDevice device) {
        this.client.connectToDevice(device, false);
        boolean result = room.addMember(this);
        if (result) {
            this.room = room;
        }
    }

    public void leaveRoom() {
        this.room.removeMember(this);
    }

    public void openRoom(Activity activity) {
        server.open(activity);
    }

    public void createGame(String roomName) {
        this.room = new Room(this);
        this.server.accept(roomName, false);
    }

    public void startGame() {
        gameController = room.startGame(this);
    }

    public void initializeGame() {
        view.showDayPage();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                view.showQuestionPage(getQuestion());
                if (gameController != null) {
                    gameController.readyToAskQuestion();
                }
            }
        }, 5000);
    }

    public void setRole(Player role) {
        this.role = role;
    }

    public String getRole() {
        return role.getRole();
    }

    public void setView(GameActivity view) {
        this.view = view;
    }

    public void submitAnswer(String answer) {
        role.setAnswer(answer);
    }

    public String getQuestion() {
        return role.getQuestion();
    }

    public void readyToStart() {
        if (gameController != null) {
            gameController.readyToStart();
        }
    }

    public void readyForNight() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                if (gameController != null) {
                    gameController.readyForNight();
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

    public void closeNightPoll(String name,  String role, String winner) {
        final String result = winner;
        view.showVictimPage(name, role);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                if (result == null) {
                    initializeGame();
                } else {
                    view.showOutro(result);
                }
            }
        }, 5000);
    }

    public void vote(String choice) {
        role.vote(choice);
    }

    public void endGame(String winner) {
        view.showOutro(winner);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            public void run() {
//                view.
//            }
//        }, 5000);
    }
}
