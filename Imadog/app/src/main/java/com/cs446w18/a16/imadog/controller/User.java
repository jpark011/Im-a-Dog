package com.cs446w18.a16.imadog.controller;

import android.os.Handler;

import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.model.Game;
import com.cs446w18.a16.imadog.model.Player;
import com.cs446w18.a16.imadog.model.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by lacie on 2018-02-18.
 */

public class User {
    private String userName;
    private Room room;
    private Game game;
    private Player role;
    private GameActivity view;

    public User(String name) {
        userName = name;
        room = null;
        game = null;
        role = null;
        view = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public void joinRoom(Room room) {
        boolean result = room.addMember(this);
        if (result) {
            this.room = room;
        }
    }

    public void leaveRoom() {
        this.room.removeMember(this);
    }

    public void createGame() {
        this.room = new Room(this);
    }

    public void startGame() {
        game = room.startGame(this);
    }

    public void initializeGame() {
        view.showDayPage();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                view.showQuestionPage(getQuestion());
                role.readyToAskQuestion();
            }
        }, 5000);
    }

    public void setRole(Player role) {
        this.role = role;
    }

    public Player getRole() {
        return role;
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
        role.readyToStart();
    }

    public void readyForNight() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                role.readyForNight();
            }
        }, 5000);
    }

    public void startPoll(String question, HashMap<String, String> answers) {
        view.showVotePage(question, answers);
    }

    public void closePoll(String name, String role) {
        view.showVictimPage(name, role);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                view.showNightPage();
                readyForNight();
            }
        }, 5000);
    }

    public void startNightPoll(String title, ArrayList<String> names) {
        view.showNightVotePage(title, names);
    }

    public void closeNightPoll(String name,  String role) {
        view.showVictimPage(name, role);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                initializeGame();
            }
        }, 5000);
    }
}
