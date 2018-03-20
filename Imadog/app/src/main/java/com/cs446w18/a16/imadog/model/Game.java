package com.cs446w18.a16.imadog.model;

import com.cs446w18.a16.imadog.controller.GameController;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.User;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private ArrayList<Dog> dogs;
    private ArrayList<Cat> cats;
    private ArrayList<String> dogQuestions;
    private ArrayList<String> catQuestions;
    private ArrayList<Player> deceased;
    private int currentDay;
    private GameController gameController;
    private boolean night;
    private String gameState;
    private String victimName;
    private String victimRole;
    private Poll poll;

    public Game(ArrayList<PlayerController> names, GameController gameController) {
        this.gameController = gameController;
        int n = names.size();

        assignRoles(names);
        setQuestions(n);

        currentDay = 1;
        night = false;
        deceased = new ArrayList<>();
        gameState = "CREATED";
        victimName = null;
        victimRole = null;
    }

    private void assignRoles(ArrayList<PlayerController> names) {
        dogs = new ArrayList<>();
        cats = new ArrayList<>();
        int total = names.size();
        int numSoFar = 0;
        Random r = new Random();
        while (total > 0) {
            int ind = r.nextInt(total);
            if (numSoFar % 5 == 0) {
                Cat cat = new Cat(this);
                cats.add(cat);
                names.get(ind).setRole(cat);
            } else {
                Dog dog = new Dog(this);
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

    public HashMap<String, String> getAnswers(boolean getCats, boolean getDogs) {
        HashMap<String, String> answers = new HashMap<>();

        if (getCats) {
            for (int i = 0; i < cats.size(); i++) {
                answers.put(cats.get(i).getName(), cats.get(i).getAnswer(currentDay));
            }
        }

        if (getDogs) {
            for (int i = 0; i < dogs.size(); i++) {
                answers.put(dogs.get(i).getName(), dogs.get(i).getAnswer(currentDay));
            }
        }

        return answers;
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

        //Collections.sort(names);
        return names;
    }

    public void killPlayer(String name) {
        if (name == null) {
            return;
        }

        int ind = -1;
        for (int i = 0; i < dogs.size(); i++) {
            if (dogs.get(i).getName() == name) {
                ind = i;
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
                    break;
                }
            }
            dead = cats.remove(ind);
        }

        dead.kill();
        deceased.add(dead);
        victimName = name;
        victimRole = dead.getRole();
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void nextDay() {
        currentDay++;
    }

    public boolean isNight() {
        return night;
    }

    public void setNight(boolean night) {
        this.night = night;
    }

    public void vote(String name, String choice) {
        poll.setVote(name, choice);
    }

    public String getWinner() {
        if (cats.size() == 0) {
            return "dogs";
        }

        if (dogs.size() == 0) {
            return "cats";
        }

        return null;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public void resetVictim() {
        victimName = null;
        victimRole = null;
    }

    public String getVictimName() {
        return victimName;
    }

    public String getVictimRole() {
        return victimRole;
    }

    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public void createPoll(ArrayList<String> names, ArrayList<String> choices) {
        this.poll = new Poll(names, choices);
    }

    public void closePoll() {
        String result = this.poll.closePoll();
        killPlayer(result);
    }

    public HashMap<String, Integer> getVoteCount() {
        return poll.getVoteCount();
    }
}
