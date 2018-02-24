package com.cs446w18.a16.imadog.model;

import android.util.Pair;

import com.cs446w18.a16.imadog.controller.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The game.
 */

public class Game {
    private ArrayList<Dog> dogs;
    private ArrayList<Cat> cats;
    private ArrayList<String> dogQuestions;
    private ArrayList<String> catQuestions;
    private ArrayList<Player> deceased;
    private int currentDay;
    private ArrayList<User> users;
    private int ready;
    private Poll poll;

    public Game(ArrayList<User> names) {
        ready = 0;
        users = new ArrayList<>(names);
        currentDay = 1;
        int n = names.size();
        assignRoles(names);
        setQuestions(20);
        deceased = new ArrayList<>();
        poll = null;
    }

    private void assignRoles(ArrayList<User> names) {
        dogs = new ArrayList<>();
        cats = new ArrayList<>();
        int total = names.size();
        int numSoFar = 0;
        Random r = new Random();
        while (total > 0) {
            int ind = r.nextInt(total);
            String name = names.get(ind).getUserName();
            if (numSoFar % 5 == 0) {
                Cat cat = new Cat(name, this);
                cats.add(cat);
                names.get(ind).setRole(cat);
            } else {
                Dog dog = new Dog(name, this);
                dogs.add(dog);
                names.get(ind).setRole(dog);
            }
            names.remove(ind);
            total = names.size();
            numSoFar++;
        }
    }

    private void setQuestions(int n) {
        dogQuestions = new ArrayList<>();
        catQuestions = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        int total = GameConstants.dogQuestions.length;
        for (int i = 0; i < total; i++) {
            count.add(i);
        }
        Random r = new Random();
        for (int j = 0; j < n; j++) {
            int ind = r.nextInt(total);
            int i = count.get(ind);
            dogQuestions.add(GameConstants.dogQuestions[i]);
            catQuestions.add(GameConstants.catQuestions[i]);
            count.remove(ind);
            total = count.size();
        }
    }

    public String getQuestion(boolean isDog, int day) {
        if (isDog) {
            return dogQuestions.get(day-1);
        }

        return catQuestions.get(day-1);
    }

    public ArrayList<Pair<String, String>> getAnswers(int day) {
        ArrayList<Pair<String, String>> ans = new ArrayList<>();
        for (int i = 0; i < cats.size(); i++) {
            ans.add(new Pair<>(cats.get(i).getName(), cats.get(i).getAnswer(day)));
        }

        for (int i = 0; i < dogs.size(); i++) {
            ans.add(new Pair<>(dogs.get(i).getName(), dogs.get(i).getAnswer(day)));
        }

        return ans;
    }

    public ArrayList<String> getPlayerNames(boolean getCats, boolean getDogs, boolean getDead) {
        ArrayList<String> names = new ArrayList<>();
        if (getDogs) {
            for (int i = 0; i < dogs.size(); i++) {
                names.add(dogs.get(i).getName());
            }
        }

        if (getCats) {
            for (int i = 0; i < cats.size(); i++) {
                names.add(cats.get(i).getName());
            }
        }

        if (getDead) {
            for (int i = 0; i < deceased.size(); i++) {
                names.add(deceased.get(i).getName());
            }
        }

        Collections.sort(names);
        return names;
    }

    public String killPlayer(String name) {
        int ind = -1;
        String role = null;
        for (int i = 0; i < dogs.size(); i++) {
            if (dogs.get(i).getName() == name) {
                ind = i;
                role = "dog";
                break;
            }
        }

        Player dead;

        if (ind >= 0) {
            dead = dogs.remove(ind);
        } else {
            for (int i = 0; i < cats.size(); i++) {
                if (cats.get(i).getName() == name) {
                    ind = i;
                    role = "cat";
                    break;
                }
            }
            dead = cats.remove(ind);
        }
        deceased.add(dead);
        return role;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void readyToStart() {
        ready++;
        int total = users.size();

        if (ready == total) {
            for (int i = 0; i < total; i++) {
                final User user = users.get(i);
                new Thread() {
                    public void run() {
                        user.initializeGame();
                    }
                }.start();
            }
            ready = 0;
        }
    }

    public void readyToAskQuestion() {
        ready++;
        final int total = users.size();

        if (ready == total) {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    Map<String, String> ans = new HashMap<>();
                    ArrayList<String> names = new ArrayList<>();

                    for (int i = 0; i < dogs.size(); i++) {
                        ans.put(dogs.get(i).getName(), dogs.get(i).getAnswer(currentDay));
                        names.add(dogs.get(i).getName());
                    }

                    for (int i = 0; i < cats.size(); i++) {
                        ans.put(cats.get(i).getName(), cats.get(i).getAnswer(currentDay));
                        names.add(cats.get(i).getName());
                    }

                    final HashMap<String, String> answers = new HashMap<>(ans);

                    poll = new Poll(names);

                    for (int i = 0; i < total; i++) {
                        final User user = users.get(i);
                        new Thread() {
                            public void run() {
                                user.startPoll(dogQuestions.get(currentDay-1), answers);
                            }
                        }.start();
                    }
                }
            }, 15000);

            timer.schedule(new TimerTask() {

                public void run() {
                    String role = null;
                    ready = 0;
                    final String result = poll.closePoll();
                    if (result != null) {
                        role = "dog"; //killPlayer(result);
                    }

                    final String victimRole = role;
                    for (int i = 0; i < users.size(); i++) {
                        final User user = users.get(i);
                        new Thread() {
                            public void run() {
                                user.closePoll(result, victimRole);
                            }
                        }.start();
                    }
                    poll = null;
                }
            }, 30000);
        }
    }

    public void readyForNight() {
        ready++;
        int total = users.size();

        if (ready == total) {
            final ArrayList<String> dogNames = getPlayerNames(false, true, false);
            final ArrayList<String> names = getPlayerNames(true, true, true);
            poll = new Poll(names);
            for (int i = 0; i < total; i++) {
                final User user = users.get(i);
                new Thread() {
                    public void run() {
                        if (user.getRole().equals("CAT")) {
                            user.startNightPoll("Vote for the dog to kill", names);
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
                    ready = 0;
                    final String result = poll.closePoll();
                    if (result != null) {
                        role = "dog"; //killPlayer(result);
                    }

                    final String victimRole = role;
                    for (int i = 0; i < users.size(); i++) {
                        final User user = users.get(i);
                        new Thread() {
                            public void run() {
                                user.closeNightPoll(result, victimRole);
                            }
                        }.start();
                    }
                    poll = null;
                    currentDay++;
                }
            }, 15000);
        }
    }
}