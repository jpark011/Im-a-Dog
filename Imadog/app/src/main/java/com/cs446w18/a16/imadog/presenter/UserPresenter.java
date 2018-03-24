package com.cs446w18.a16.imadog.presenter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.util.Pair;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.activities.menu.LobbyActivity;
import com.cs446w18.a16.imadog.bluetooth.Bluetooth;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.commands.LeaveLobbyCommand;
import com.cs446w18.a16.imadog.commands.SendMessageCommand;
import com.cs446w18.a16.imadog.commands.SetUsernameCommand;
import com.cs446w18.a16.imadog.commands.SubmitAnswerCommand;
import com.cs446w18.a16.imadog.commands.VoteCommand;
import com.cs446w18.a16.imadog.model.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UserPresenter {
    private String userName;
    private String clientName;
    private GameActivity view;
    private Bluetooth client;
    private BluetoothServer server;
    private boolean isServer;
    private PlayerPresenter hostPlayer;
    private LobbyActivity lobby;
    private GamePresenter gamePresenter;
    private String currentRole;
    private boolean currentDead;
    private HashMap<String, String> currentPollAnswers;

    public UserPresenter(String name) {
        this.userName = name;
        view = null;
        client = null;
        server = null;
        currentPollAnswers = null;
        currentRole = null;
        clientName = null;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
        if (userName == null) {
            this.userName = clientName;
        }

        Command cmd = new SetUsernameCommand(clientName, userName);
        client.send(cmd);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
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

    public void leaveRoom() {
        if (isServer()) {
            server.leaveRoom();
            server = null;
        } else {
            Command cmd = new LeaveLobbyCommand(this.clientName);
            sendCommand(cmd);
            clientName = null;
            client = null;
        }
        lobby.toMainActivity();
    }

    public void openRoom(Activity activity) {
        server.open(activity);
    }

    public void closeRoom() {
        this.client = null;
        this.currentRole = null;
        lobby.toMainActivity();
    }

    public ArrayList<String> getRoomMembers() {
        return server.getMembers();
    }

    public void updateRoomMembers(ArrayList<String> members) {
        lobby.updateLobbyMembers(members);
    }

    public void setGamePresenter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    public void createGame(String roomName) {
        this.server.accept(roomName, false);
    }

    public void startGame() {
        hostPlayer = this.server.startGame();
        gamePresenter.readyToStart();
    }

    public void initializeGame(String question, String role) {
        this.currentRole = role;
        this.currentDead = false;
        final String q = question;
        lobby.openGameActivity();
        if (!isServer) {
            client.setCommunicationCallback(new ClientCommunicationCallback());
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                readyForDay(q);
            }
        }, GameConstants.introDuration);
    }

    public void setView(GameActivity view) {
        this.view = view;
    }

    public void setLobby(LobbyActivity lobby) {
        this.lobby = lobby;
    }

    public String getRole() {
        return this.currentRole;
    }

    public boolean isDead() {
        return this.currentDead;
    }

    public void setDead() {
        this.currentDead = true;
    }

    public HashMap<String, String> getPollAnswers() {
        return currentPollAnswers;
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
        }, GameConstants.dayNightDuration);
    }

    public void startPoll(String question, HashMap<String, String> answers, HashMap<String, Integer> votes) {
        view.showVotePage(question, votes, answers);
        this.currentPollAnswers = answers;
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
                    endGame();
                }
            }
        }, GameConstants.victimPageDuration);
    }

    public void startNightPoll(String title, HashMap<String, Integer> votes) {
        view.showNightVotePage(title, votes);
        this.currentPollAnswers = null;
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
                    endGame();
                }
            }
        }, GameConstants.victimPageDuration);
    }

    public void endGame() {
        if (isServer()) {
            hostPlayer.endGame();
        }
    }

    public void vote(String choice) {
        Command cmd = new VoteCommand(choice);
        sendCommand(cmd);
    }

    public void updatePoll(HashMap<String, Integer> votes) {
        view.updatePollVotes(votes);
    }

    public void sendMessage(String text) {
        Command cmd = new SendMessageCommand(text);
        sendCommand(cmd);
    }

    public void updateChat(ArrayList<String> names, ArrayList<String> messages) {
        ArrayList<Pair<String, String>> history = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            history.add(new Pair<>(names.get(i), messages.get(i)));
        }
        view.updateChat(history);
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
            System.out.println("DISCONNECT: "+message);
        }

        @Override
        public void onMessage(Command command) {
            command.setReceiver(Global.user);
            command.execute();
        }

        @Override
        public void onError(String message) {
            System.out.println("ERROR: "+message);
        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            System.out.println("SYSTEM ERROR: "+message);
        }

        @Override
        public void onAccept(String playerName) {

        }
    }
}
