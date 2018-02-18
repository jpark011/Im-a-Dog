package com.cs446w18.a16.imadog.model;

import android.util.Pair;

import com.cs446w18.a16.imadog.controller.User;

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

    public Game(ArrayList<User> names) {
        currentDay = 1;
        int n = names.size();
        assignRoles(names);
        setQuestions(n);
        deceased = new ArrayList<Player>();
        for (int i = 0; i < names.size(); i++) {
            names.get(i).startGame(this);
        }
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
        ArrayList<Integer> count = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            count.set(i, i);
        }
        Random r = new Random();

        int total = count.size();
        while (total > 0) {
            int ind = r.nextInt(total);
            int i = count.get(ind);
            dogQuestions.add(GameConstants.dogQuestions[i]);
            catQuestions.add(GameConstants.catQuestions[i]);
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

    public void killPlayer(String name) {
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
        deceased.add(dead);
    }

    public int getCurrentDay() {
        return currentDay;
    }
}
