package com.cs446w18.a16.imadog.presenter;

import android.bluetooth.BluetoothDevice;
import android.util.Pair;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.commands.CloseDayPollCommand;
import com.cs446w18.a16.imadog.commands.CloseNightPollCommand;
import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.commands.InitializeCommand;
import com.cs446w18.a16.imadog.commands.KillPlayerCommand;
import com.cs446w18.a16.imadog.commands.StartDayPollCommand;
import com.cs446w18.a16.imadog.commands.StartNightPollCommand;
import com.cs446w18.a16.imadog.commands.UpdateChatCommand;
import com.cs446w18.a16.imadog.commands.UpdatePollCommand;
import com.cs446w18.a16.imadog.model.Chat;
import com.cs446w18.a16.imadog.model.Message;
import com.cs446w18.a16.imadog.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerPresenter {
    private String username;
    private Player role;
    private BluetoothServer server;
    private String clientName;
    private Chat chat;

    public PlayerPresenter(BluetoothServer server, String clientName) {
        this.clientName = clientName;
        this.server = server;
        role = null;
        chat = null;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public String getUserName() {
        return username;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void sendMessage(String text) {
        this.chat.addMessage(text, getUserName());
    }

    public void updateChat() {
        ArrayList<Message> history = chat.getMessages();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> messages = new ArrayList<>();

        for (Message message: history) {
            names.add(message.getName());
            messages.add(message.getText());
        }

        Command cmd = new UpdateChatCommand(names, messages);
        sendCommand(cmd);
    }

    public void initializeGame() {
        if (server != null) {
            server.setCommunicationCallbacks(clientName, new ServerCommunicationCallback());
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
        HashMap<String, Integer> count = role.getVoteCount();
        Command cmd = new StartDayPollCommand(question, answers, count);
        sendCommand(cmd);
    }

    public void closePoll() {
        String victimName = role.getVictimName();
        String victimRole = role.getVictimRole();
        String winner = role.getWinner();

        Command cmd = new CloseDayPollCommand(victimName, victimRole, winner);
        sendCommand(cmd);

        if (role.isDead()) {
            Command killCmd = new KillPlayerCommand();
            sendCommand(killCmd);
        }
    }

    public void startNightPoll() {
        String title = role.getNightPollTitle();
        HashMap<String, Integer> votes = role.getVoteCount();
        Command cmd = new StartNightPollCommand(title, votes);
        sendCommand(cmd);
    }

    public void closeNightPoll() {
        String victimName = role.getVictimName();
        String victimRole = role.getVictimRole();
        String winner = role.getWinner();
        String question = getQuestion();

        Command cmd = new CloseNightPollCommand(victimName, victimRole, winner, question);
        sendCommand(cmd);

        if (role.isDead()) {
            Command killCmd = new KillPlayerCommand();
            sendCommand(killCmd);
        }
    }

    public void vote(String choice) {
        role.vote(choice);
    }

    public void updatePoll() {
        Command cmd = new UpdatePollCommand(role.getVoteCount());
        sendCommand(cmd);
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
            case "UPDATING_POLL":
                updatePoll();
                break;
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
            command.setReceiver(PlayerPresenter.this);
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
