package com.cs446w18.a16.imadog.controller;

import android.bluetooth.BluetoothSocket;

import com.cs446w18.a16.imadog.model.Command;
import com.cs446w18.a16.imadog.model.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lacie Yi on 2018-02-25.
 */

public class UserHost {

    private String userName;
    private Player role;
    private GameController gameController;
    private BluetoothSocket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    boolean isHost;
    UserClient client;

    public UserHost(BluetoothSocket socket, UserClient client, boolean isHost) {
        userName = null;
        role = null;
        gameController = null;
        this.isHost = isHost;
        if (isHost) {
            this.client = client;
        } else {
            this.socket = socket;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (Exception e) {

            }
        }
    }

    public void getInput() {
        if (!isHost) {
            try {
                Command command = (Command) ois.readObject();
                switch (command.getCommand()) {
                    case "SET_USERNAME":
                        setUserName((String) command.getArgs().get(0));
                        break;
                    case "SUBMIT_ANSWER":
                        submitAnswer((String) command.getArgs().get(0));
                        break;
                    case "GET_QUESTION":
                        getQuestion();
                        break;
                    case "READY_TO_START":
                        readyToStart();
                        break;
                    case "READY_FOR_NIGHT":
                        readyForNight();
                        break;
                    case "VOTE":
                        vote((String) command.getArgs().get(0));
                        break;
                    case "READY_TO_ASK_QUESTION":
                        readyToAskQuestion();
                        break;
                }
            } catch (Exception e) {}
        }
    }

    public String getUserName() {
        if (userName == null) {
            ArrayList<Object> wArgs = new ArrayList<>();
            sendCommand("GET_USERNAME", wArgs);
            try {
                Command command = (Command) ois.readObject();
                userName = (String)command.getArgs().get(0);
            } catch (Exception e) {}
        }
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public void setGame(GameController g) {
        gameController = g;
    }

    public void initializeGame() {
        if (isHost) {
            client.initializeGame();
        } else {
            ArrayList<Object> args = new ArrayList<>();
            sendCommand("INITIALIZE_GAME", args);
        }
    }

    public void readyToAskQuestion(){
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
        if (isHost) {
            return role.getQuestion();
        }
        ArrayList<Object> args = new ArrayList<>();
        args.add(role.getQuestion());
        sendCommand("RECEIVE_QUESTION", args);
        return null;
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
        if (isHost) {
            client.startPoll(question, answers);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(question);
            args.add(answers);
            sendCommand("START_DAY_POLL", args);
        }
    }

    public void closePoll(String name, String role, String winner) {
        if (isHost) {
            client.closePoll(name, role, winner);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(name);
            args.add(role);
            args.add(winner);
            sendCommand("CLOSE_DAY_POLL", args);
        }
    }

    public void startNightPoll(String title, ArrayList<String> names) {
        if (isHost) {
            client.startNightPoll(title, names);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(title);
            args.add(names);
            sendCommand("START_NIGHT_POLL", args);
        }
    }

    public void closeNightPoll(String name,  String role, String winner) {
        if (isHost) {
            client.closeNightPoll(name, role, winner);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(name);
            args.add(role);
            args.add(winner);
            sendCommand("CLOSE_NIGHT_POLL", args);
        }
    }

    public void vote(String choice) {
        role.vote(choice);
    }

    public void endGame(String winner) {
        if (isHost)  {
            client.endGame(winner);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(winner);
            sendCommand("END_GAME", args);
        }
    }

    private void sendCommand(String comm, ArrayList<Object> args) {
        try {
            Command c = new Command(comm, args);
            oos.writeObject(c);
        } catch(Exception e) {}
    }
}
