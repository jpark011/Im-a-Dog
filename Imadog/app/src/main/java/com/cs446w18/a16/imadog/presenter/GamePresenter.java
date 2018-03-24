package com.cs446w18.a16.imadog.presenter;

import com.cs446w18.a16.imadog.model.Game;
import com.cs446w18.a16.imadog.model.GameConstants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GamePresenter {
    private ArrayList<PlayerPresenter> observers;
    private Game game;

    public GamePresenter(ArrayList<PlayerPresenter> users) {
        this.observers = new ArrayList<>(users);
        game = new Game(users, this);
    }

    // Notifies all the player presenters there is a change in the game state
    private void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            final PlayerPresenter observer = observers.get(i);
            new Thread() {
                public void run() {
                    observer.update();
                }
            }.start();
        }
    }

    // Start the timer to start the intro, once timer runs out, we are ready to go ask a question
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

    // Start the timer for asking a question. After questionPageDuration seconds,
    // we will start the voting for the day. After dayPollPageDuration seconds,
    // we will end the voting, show the results, and either go to the Night (readyForNight()) or end the game
    public void readyToAskQuestion() {
        game.setNight(false);
        game.resetVictim();
        int duration = GameConstants.questionPageDuration;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                ArrayList<String> names = game.getPlayerNames(true,true, false);
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
            }
        }, duration);
        duration += GameConstants.victimPageDuration + GameConstants.dayNightDuration;
        timer.schedule(new TimerTask() {

            public void run() {
                if (game.getWinner() == null) readyForNight();
            }
        }, duration);
    }

    // Start the voting for the night. After nightPollPageDuration second,
    // we will end the voting, show the results, and go to either go to
    // the next Day(readyToAskQuestion()) or end the game
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
                game.closePoll();
                game.setGameState("CLOSING_NIGHT_POLL");
                notifyObservers();
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

    // The poll has been updated, update everyone's voting pages
    public void updatePoll() {
        game.setGameState("UPDATING_POLL");
        notifyObservers();
    }
}
