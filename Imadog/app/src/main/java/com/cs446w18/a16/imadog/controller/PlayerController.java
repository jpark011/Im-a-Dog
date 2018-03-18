package com.cs446w18.a16.imadog.controller;

import android.bluetooth.BluetoothDevice;

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

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerController {
    private String username;
    private Player role;
    private BluetoothServer server;
    private String clientName;

    public PlayerController(BluetoothServer server, String clientName) {
        this.clientName = clientName;
        this.server = server;
        role = null;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public void initializeGame() {
        if (server != null) {
            server.setCommunicationCallbacks(clientName, new ServerCommunicationCallback(this));
        }
        Command cmd = new InitializeCommand(getQuestion(), role.getRole());
        sendCommand(cmd);
    }

    public void setRole(Player role) {
        this.role = role;
        role.setName(username);
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

    public void startPoll() {
        String question = role.getDayPollTitle();
        HashMap<String, String> answers = role.getDayPollAnswers();
        Command cmd = new StartDayPollCommand(question, answers);
        sendCommand(cmd);
    }

    public void closePoll() {
        String victimName = role.getVictimName();
        String victimRole = role.getVictimRole();
        String winner = role.getWinner();
        Command cmd = new CloseDayPollCommand(victimName, victimRole, winner);
        sendCommand(cmd);
    }

    public void startNightPoll() {
        String title = role.getNightPollTitle();
        ArrayList<String> names = role.getNightPollChoices();
        Command cmd = new StartNightPollCommand(title, names);
        sendCommand(cmd);
    }

    public void closeNightPoll() {
        String victimName = role.getVictimName();
        String victimRole = role.getVictimRole();
        String winner = role.getWinner();
        String question = getQuestion();
        Command cmd = new CloseNightPollCommand(victimName, victimRole, winner, question);
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

    public void update() {
        switch(role.getGameState()) {
            case "INITIALIZE":
                initializeGame();
                break;
            case "STARTING_DAY_POLL":
                startPoll();
                break;
            case "CLOSING_DAY_POLL":
                closePoll();
                break;
            case "STARTING_NIGHT_POLL":
                startNightPoll();
                break;
            case "CLOSING_NIGHT_POLL":
                closeNightPoll();
                break;
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
