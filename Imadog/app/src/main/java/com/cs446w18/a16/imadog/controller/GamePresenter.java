package com.cs446w18.a16.imadog.controller;

import com.cs446w18.a16.imadog.model.Chat;
import com.cs446w18.a16.imadog.model.Game;
import com.cs446w18.a16.imadog.model.GameConstants;
import com.cs446w18.a16.imadog.model.Poll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GamePresenter {
    private ArrayList<PlayerController> observers;
    private Game game;
    private Poll poll;

    public GamePresenter(ArrayList<PlayerController> users) {
        this.observers = new ArrayList<>(users);
        game = new Game(users, this);
        poll = null;
    }

    private void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            final PlayerController observer = observers.get(i);
            new Thread() {
                public void run() {
                    observer.update();
                }
            }.start();
        }
    }

    public void readyToStart() {
        game.setGameState("INITIALIZE");
        notifyObservers();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                readyToAskQuestion();
            }
        }, GameConstants.introDuration + GameConstants.dayNightDuration);
    }

    public void readyToAskQuestion() {
        game.setNight(false);
        game.resetVictim();
        int duration = GameConstants.questionPageDuration;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                ArrayList<String> names = game.getPlayerNames(true,true, false);
                //poll = new Poll(names, names);
                game.createPoll(names, names);
                game.setGameState("STARTING_DAY_POLL");
                notifyObservers();
            }
        }, duration);
        duration += GameConstants.dayPollPageDuration;
        timer.schedule(new TimerTask() {

            public void run() {
                game.closePoll();
                game.setGameState("CLOSING_DAY_POLL");
                notifyObservers();
                //poll = null;
            }
        }, duration);
        duration += GameConstants.victimPageDuration + GameConstants.dayNightDuration;
        timer.schedule(new TimerTask() {

            public void run() {
                if (game.getWinner() == null) readyForNight();
            }
        }, duration);
    }

    public void readyForNight() {
        game.setNight(true);
        game.resetVictim();
        final ArrayList<String> dogNames = game.getPlayerNames(false, true, false);
        final ArrayList<String> names = game.getPlayerNames(true, true, false);
        game.createPoll(names, dogNames);
        game.setGameState("STARTING_NIGHT_POLL");
        notifyObservers();

        int duration = GameConstants.nightPollPageDuration;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                //final String result = poll.closePoll();
                game.closePoll();
                game.setGameState("CLOSING_NIGHT_POLL");
                notifyObservers();
                //poll = null;
                game.nextDay();
            }
        }, duration);
        duration += GameConstants.victimPageDuration + GameConstants.dayNightDuration;
        timer.schedule(new TimerTask() {

            public void run() {
                if (game.getWinner() == null) readyToAskQuestion();
            }
        }, duration);
    }

    public void vote(String name, String choice) {
        poll.setVote(name, choice);
    }
}
