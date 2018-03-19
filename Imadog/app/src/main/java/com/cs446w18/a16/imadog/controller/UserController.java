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
import com.cs446w18.a16.imadog.commands.SendMessageCommand;
import com.cs446w18.a16.imadog.commands.SetUsernameCommand;
import com.cs446w18.a16.imadog.commands.SubmitAnswerCommand;
import com.cs446w18.a16.imadog.commands.VoteCommand;
import com.cs446w18.a16.imadog.model.GameConstants;
import com.cs446w18.a16.imadog.model.GameState;
import com.cs446w18.a16.imadog.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UserController {
    private String userName;
    private GameActivity view;
    private Bluetooth client;
    private BluetoothServer server;
    private boolean isServer;
    private PlayerController hostPlayer;
    private LobbyActivity lobby;
    private GameController gameController;
    private GameState gameState;

    public UserController(String name) {
        this.userName = name;
        view = null;
        client = null;
        server = null;
        gameState = null;
    }

    public void setClientName(String clientName) {
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

    public void initializeGame(String question, String role) {
        gameState = new GameState(role);
        gameState.setInGame(true);
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
        return gameState.getRole();
    }

    public void submitAnswer(String answer) {
        Command cmd = new SubmitAnswerCommand(answer);
        sendCommand(cmd);
    }

    public void readyForDay(String question) {
        gameState.setCurrentQuestion(question);
        view.showDayPage();
        gameState.setGameFragment(GameConstants.DAY_PAGE);
        gameState.setCurrentDuration(GameConstants.dayNightDuration);
        gameState.setCurrentStartTimer();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                view.showQuestionPage(gameState.getCurrentQuestion());
                gameState.setGameFragment(GameConstants.QUESTION_PAGE);
                gameState.setCurrentDuration(GameConstants.questionPageDuration);
                gameState.setCurrentStartTimer();
            }
        }, GameConstants.dayNightDuration);
    }

    public void startPoll(String question, HashMap<String, String> answers) {
        view.showVotePage(question, answers);
        gameState.setCurrentPollTitle(question);
        gameState.setCurrentPollAnswers(answers);
        gameState.setGameFragment(GameConstants.DAY_VOTE_PAGE);
        gameState.setCurrentDuration(GameConstants.dayPollPageDuration);
        gameState.setCurrentStartTimer();
    }

    public void closePoll(String name, String role, String winner) {
        final String result = winner;
        gameState.setCurrentVictimName(name);
        gameState.setCurrentVictimRole(role);
        view.showVictimPage(name, role);
        gameState.setGameFragment(GameConstants.VICTIM_PAGE);
        gameState.setCurrentDuration(GameConstants.victimPageDuration);
        gameState.setCurrentStartTimer();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                if (result == null) {
                    view.showNightPage();
                    gameState.setGameFragment(GameConstants.NIGHT_PAGE);
                    gameState.setCurrentDuration(GameConstants.dayNightDuration);
                    gameState.setCurrentStartTimer();
                } else {
                    view.showOutro(result);
                    gameState.setInGame(false);
                }
            }
        }, GameConstants.victimPageDuration);
    }

    public void startNightPoll(String title, ArrayList<String> names) {
        gameState.setCurrentPollTitle(title);
        gameState.setCurrentPollNames(names);
        view.showNightVotePage(title, names);
        gameState.setGameFragment(GameConstants.NIGHT_VOTE_PAGE);
        gameState.setCurrentDuration(GameConstants.nightPollPageDuration);
        gameState.setCurrentStartTimer();
    }

    public void closeNightPoll(String name,  String role, String winner, String question) {
        gameState.setCurrentQuestion(question);
        gameState.setCurrentVictimName(name);
        gameState.setCurrentVictimRole(role);
        final String result = winner;
        view.showVictimPage(name, role);
        gameState.setGameFragment(GameConstants.VICTIM_PAGE);
        gameState.setCurrentDuration(GameConstants.victimPageDuration);
        gameState.setCurrentStartTimer();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                if (result == null) {
                    readyForDay(gameState.getCurrentQuestion());
                } else {
                    view.showOutro(result);
                    gameState.setInGame(false);
                }
            }
        }, GameConstants.victimPageDuration);
    }

    public void vote(String choice) {
        Command cmd = new VoteCommand(choice);
        sendCommand(cmd);
    }

    public void sendMessage(String text) {
        Command cmd = new SendMessageCommand(text);
        sendCommand(cmd);
    }

    public void updateChat(ArrayList<Message> history) {}

    public void switchToGame() {
        Date currentTime = new Date();
        int remainingTime = (int)gameState.getCurrentStartTimer().getTime() - (int)currentTime.getTime();
        switch (gameState.getGameFragment()) {
            case GameConstants.DAY_PAGE:
                view.showDayPage();
                break;
            case GameConstants.DAY_VOTE_PAGE:
                view.showVotePage(gameState.getCurrentPollTitle(), gameState.getCurrentPollAnswers());
                break;
            case GameConstants.NIGHT_PAGE:
                view.showNightPage();
                break;
            case GameConstants.NIGHT_VOTE_PAGE:
                view.showNightVotePage(gameState.getCurrentPollTitle(), gameState.getCurrentPollNames());
                break;
            case GameConstants.QUESTION_PAGE:
                view.showQuestionPage(gameState.getCurrentQuestion());
                break;
            case GameConstants.VICTIM_PAGE:
                view.showVictimPage(gameState.getCurrentVictimName(), gameState.getCurrentVictimRole());
                break;
        }
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
