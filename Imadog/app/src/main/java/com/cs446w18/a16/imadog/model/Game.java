package com.cs446w18.a16.imadog.model;

import com.cs446w18.a16.imadog.controller.GameController;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.User;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

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
    private GameController gameController;
    private boolean night;

    public Game(ArrayList<PlayerController> names, GameController gameController) {
        currentDay = 1;
        night = false;
        int n = names.size();
        this.gameController = gameController;
        assignRoles(names);
        setQuestions(n);
        deceased = new ArrayList<>();
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
                System.out.println("Name: "+ cats.get(i).getName());
                answers.put(cats.get(i).getName(), cats.get(i).getAnswer(currentDay));
            }
        }

        if (getDogs) {
            for (int i = 0; i < dogs.size(); i++) {
                System.out.println("Name: "+ dogs.get(i).getName());
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

        dead.kill();
        deceased.add(dead);
        return role;
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
        gameController.vote(name, choice);
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
}
