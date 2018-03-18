package com.cs446w18.a16.imadog.controller;

import com.cs446w18.a16.imadog.model.Game;
import com.cs446w18.a16.imadog.model.Poll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lacie on 2018-02-24.
 */

public class GameController {
    private ArrayList<PlayerController> observers;
    private Game game;
    Poll poll;

    public GameController(ArrayList<PlayerController> users) {
        this.observers = new ArrayList<>(users);
        game = new Game(users, this);
        poll = null;
    }

    public void registerObserver(PlayerController observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unRegisterObserver(PlayerController observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
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
        }, 10000);
    }

    public void readyToAskQuestion() {
        game.setNight(false);
        game.resetVictim();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                ArrayList<String> names = game.getPlayerNames(true,true, false);
                poll = new Poll(names, names);
                game.setGameState("STARTING_DAY_POLL");
                notifyObservers();
            }
        }, 15000);

        timer.schedule(new TimerTask() {

            public void run() {
                final String result = poll.closePoll();
                game.killPlayer(result);
                game.setGameState("CLOSING_DAY_POLL");
                notifyObservers();
                poll = null;
            }
        }, 30000);

        timer.schedule(new TimerTask() {

            public void run() {
                if (game.getWinner() == null) readyForNight();
            }
        }, 40000);
    }

    public void readyForNight() {
        game.setNight(true);
        game.resetVictim();
        final ArrayList<String> dogNames = game.getPlayerNames(false, true, false);
        final ArrayList<String> names = game.getPlayerNames(true, true, false);
        poll = new Poll(names, dogNames);
        game.setGameState("STARTING_NIGHT_POLL");
        notifyObservers();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                final String result = poll.closePoll();
                game.killPlayer(result);
                game.setGameState("CLOSING_NIGHT_POLL");
                notifyObservers();
                poll = null;
                game.nextDay();
            }
        }, 15000);
        timer.schedule(new TimerTask() {

            public void run() {
                if (game.getWinner() == null) readyToAskQuestion();
            }
        }, 25000);
    }

    public void vote(String name, String choice) {
        poll.setVote(name, choice);
    }
}
