package com.cs446w18.a16.imadog.controller;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.model.Command;
import com.cs446w18.a16.imadog.model.Player;
import com.cs446w18.a16.imadog.model.Room;

import java.io.IOException;
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
        room = null;
        role = null;
        view = null;
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

    public void getInput() throws Exception{
        if (!isHost) {
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
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public void setGame(GameController g) {
        gameController = g;
    }

    public void initializeGame() throws IOException {
        if (isHost) {
            client.initializeGame();
        } else {
            ArrayList<Object> args = new ArrayList<>();
            Command command = new Command("INITIALIZE_GAME", args);
            oos.writeObject(command);
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

    public String getQuestion() throws Exception {
        if (isHost) {
            return role.getQuestion();
        }
        ArrayList<Object> args = new ArrayList<>();
        args.add(role.getQuestion());
        Command command = new Command("RECEIVE_QUESTION", args);
        oos.writeObject(command);
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

    public void startPoll(String question, HashMap<String, String> answers) throws IOException {
        if (isHost) {
            client.startPoll(question, answers);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(question);
            args.add(answers);
            Command command = new Command("START_DAY_POLL", args);
            oos.writeObject(command);
        }
    }

    public void closePoll(String name, String role, String winner) throws IOException {
        if (isHost) {
            client.closePoll(name, role, winner);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(name);
            args.add(role);
            args.add(winner);
            Command command = new Command("CLOSE_DAY_POLL", args);
            oos.writeObject(command);
        }
    }

    public void startNightPoll(String title, ArrayList<String> names) throws IOException {
        if (isHost) {
            client.startNightPoll(title, names);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(title);
            args.add(names);
            Command command = new Command("START_NIGHT_POLL", args);
            oos.writeObject(command);
        }
    }

    public void closeNightPoll(String name,  String role, String winner) throws IOException {
        if (isHost) {
            client.closeNightPoll(name, role, winner);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(name);
            args.add(role);
            args.add(winner);
            Command command = new Command("CLOSE_NIGHT_POLL", args);
            oos.writeObject(command);
        }
    }

    public void vote(String choice) {
        role.vote(choice);
    }

    public void endGame(String winner) throws IOException {
        if (isHost)  {
            client.endGame(winner);
        } else {
            ArrayList<Object> args = new ArrayList<>();
            args.add(winner);
            Command command = new Command("END_GAME", args);
            oos.writeObject(command);
        }
    }
}
