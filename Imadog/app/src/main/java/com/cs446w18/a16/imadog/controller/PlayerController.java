package com.cs446w18.a16.imadog.controller;

import android.bluetooth.BluetoothSocket;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lacie on 2018-03-15.
 */

public class PlayerController {
    private String userName;
    private Player role;
    private GameController gameController;
    private BluetoothServer server;
    private BluetoothSocket socket;
    private boolean isServer;
    private UserController hostUser;

    public PlayerController(BluetoothServer server, BluetoothSocket socket) {
        this.server = server;
        this.socket = socket;
        hostUser = null;
        isServer = false;
        role = null;
        gameController = null;
    }

    public void setHost(GameController gameController, UserController hostUser) {
        this.gameController = gameController;
        this.hostUser = hostUser;
        isServer = true;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public void initializeGame() {
        sendCommand("INITIALIZE_GAME", getQuestion());
    }

    public void readyToAskQuestion() {
        gameController.readyToAskQuestion();
    }

    public void setRole(Player role) {
        this.role = role;
    }

    public String getRole() {
        return role.getRole();
    }

    public void submitAnswer(String answer) {
        role.setAnswer(answer);
    }

    public String getQuestion() {
        return role.getQuestion();
    }

    public void readyToStart() {
        gameController.readyToStart();
    }

    public void startNight() {
        gameController.readyForNight();
    }

    public void startPoll(String question, HashMap<String, String> answers) {
        sendCommand("START_POLL", question, answers);
    }

    public void closePoll(String name, String role, String winner) {
        sendCommand("CLOSE_POLL", name, role, winner);
    }

    public void startNightPoll(String title, ArrayList<String> names) {
        sendCommand("START_NIGHT_POLL", title, names);
    }

    public void closeNightPoll(String name,  String role, String winner) {
        sendCommand("CLOSE_NIGHT_POLL", name, role, winner, getQuestion());
    }

    public void vote(String choice) {
        role.vote(choice);
    }

    public void endGame(String winner) {
        sendCommand("END_GAME", winner);
    }

    public void notify(Command cmd) {
        String action = cmd.getAction();
        Object[] payload = cmd.getPayload();
        switch(action) {
            case "SET_USERNAME":
                setUserName((String)payload[0]);
                break;
            case "SUBMIT_ANSWER":
                submitAnswer((String)payload[0]);
                break;
            case "VOTE":
                vote((String)payload[0]);
                break;
        }
    }

    public void sendCommand(String action, Object... payload) {
        Command cmd = new Command(action, payload);
        if (isServer) {
            hostUser.notify(cmd);
        } else {
            server.send(cmd, socket);
        }
    }
}
