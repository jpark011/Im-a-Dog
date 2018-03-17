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
    private ArrayList<PlayerController> users;
    private Game game;
    Poll poll;

    public GameController(ArrayList<PlayerController> users) {
        this.users = new ArrayList<>(users);
        game = new Game(users, this);
        poll = null;
    }

    public void readyToStart() {
        int total = users.size();

        for (int i = 0; i < total; i++) {
            final PlayerController user = users.get(i);
            new Thread() {
                public void run() {
                    user.initializeGame();
                }
            }.start();
        }
    }

    public void readyToAskQuestion() {
        game.setNight(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                final HashMap<String, String> answers = game.getAnswers(true,true);
                ArrayList<String> names = game.getPlayerNames(true,true, false);

                poll = new Poll(names, names);
                final String dogQuestion = game.getQuestion(true, game.getCurrentDay());

                for (int i = 0; i < users.size(); i++) {
                    final PlayerController user = users.get(i);
                    new Thread() {
                        public void run() {
                            user.startPoll(dogQuestion, answers);
                        }
                    }.start();
                }
            }
        }, 15000);

        timer.schedule(new TimerTask() {

            public void run() {
                String role = null;
                final String result = poll.closePoll();
                if (result != null) {
                    role = game.killPlayer(result);
                }

                final String winner = game.getWinner();
                final String victimRole = role;
                for (int i = 0; i < users.size(); i++) {
                    final PlayerController user = users.get(i);
                    new Thread() {
                        public void run() {
                            user.closePoll(result, victimRole, winner);
                        }
                    }.start();
                }
                poll = null;
            }
        }, 30000);
    }

    public void readyForNight() {
        game.setNight(true);
        final ArrayList<String> dogNames = game.getPlayerNames(false, true, false);
        final ArrayList<String> names = game.getPlayerNames(true, true, false);
        poll = new Poll(names, names);
        for (int i = 0; i < users.size(); i++) {
            final PlayerController user = users.get(i);
            new Thread() {
                public void run() {
                    if (user.getRole().equals("CAT")) {
                        user.startNightPoll("Vote for the dog to kill", dogNames);
                    } else {
                        user.startNightPoll("Vote for the best answer", names);
                    }
                }
            }.start();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                String role = null;
                final String result = poll.closePoll();
                if (result != null) {
                    role = game.killPlayer(result);
                }

                final String winner = game.getWinner();
                final String victimRole = role;
                for (int i = 0; i < users.size(); i++) {
                    final PlayerController user = users.get(i);
                    new Thread() {
                        public void run() {
                            user.closeNightPoll(result, victimRole, winner);
                        }
                    }.start();
                }
                poll = null;
                game.nextDay();
            }
        }, 15000);
    }

    public void vote(String name, String choice) {
        poll.setVote(name, choice);
    }
}
