package com.cs446w18.a16.imadog.controller;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.cs446w18.a16.imadog.Global;
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
    private Player role;
    private BluetoothServer server;
    private String clientName;

    public PlayerController(BluetoothServer server, String clientName) {
        this.clientName = clientName;
        this.server = server;
        role = null;
    }

    public void setUserName(String name) {
        role.setName(name);
    }

    public void initializeGame() {
        if (server != null) {
            server.setCommunicationCallbacks(clientName, new ServerCommunicationCallback(this));
        }
        Command cmd = new InitializeCommand(getQuestion());
        sendCommand(cmd);
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
        if (server == null) {
            cmd.setReceiver(Global.user);
            cmd.execute();
        } else {
            server.send(cmd, clientName);
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
            command.setReceiver(PlayerController.this);
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
