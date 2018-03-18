package com.cs446w18.a16.imadog.controller;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.commands.CloseDayPollCommand;
import com.cs446w18.a16.imadog.commands.CloseNightPollCommand;
import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.commands.InitializeCommand;
import com.cs446w18.a16.imadog.commands.StartDayPollCommand;
import com.cs446w18.a16.imadog.commands.StartNightPollCommand;
import com.cs446w18.a16.imadog.model.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private ObjectOutputStream out;
    private boolean isServer;
    private UserController hostUser;
    private String clientName;

    public PlayerController(BluetoothServer server, ObjectOutputStream out, String clientName) {
        this.clientName = clientName;
        this.server = server;
        hostUser = null;
        isServer = false;
        role = null;
        gameController = null;

        this.out = out;
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
        role.setName(name);
        System.out.println("SET USERNAME: "+name);
    }

    public void initializeGame() {
        if (!isServer) {
            server.setCommunicationCallbacks(clientName, new ServerCommunicationCallback(this));
        }
        Command cmd = new InitializeCommand(getQuestion());
        sendCommand(cmd);
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
        Command cmd = new StartDayPollCommand(question, answers);
        sendCommand(cmd);
    }

    public void closePoll(String name, String role, String winner) {
        Command cmd = new CloseDayPollCommand(name, role, winner);
        sendCommand(cmd);
    }

    public void startNightPoll(String title, ArrayList<String> names) {
        Command cmd = new StartNightPollCommand(title, names);
        sendCommand(cmd);
    }

    public void closeNightPoll(String name,  String role, String winner) {
        Command cmd = new CloseNightPollCommand(name, role, winner, getQuestion());
        sendCommand(cmd);
    }

    public void vote(String choice) {
        role.vote(choice);
    }

    public void sendCommand(Command cmd) {
        if (isServer) {
            cmd.execute();
        } else {
            server.send(cmd, out);
        }
    }

    private class ServerCommunicationCallback implements CommunicationCallback {
        PlayerController playerController;

        public ServerCommunicationCallback(PlayerController playerController) {
            this.playerController = playerController;
        }

        @Override
        public void onConnect(BluetoothDevice device) {

        }

        @Override
        public void onDisconnect(BluetoothDevice device, String message) {

        }

        @Override
        public void onMessage(Command command) {
            command.execute(PlayerController.this);
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
