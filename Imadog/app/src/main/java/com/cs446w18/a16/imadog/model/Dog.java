package com.cs446w18.a16.imadog.model;

/**
 * Created by lacie on 2018-02-16.
 */

public class Dog extends Player {
    public Dog(String n, Game g) {
        super(n, g);
    }

    public String getRole() {
        return "Dog";
    }

    public String getQuestion() {
        int day = game.getCurrentDay();
        return game.getQuestion(true, day);
    }
}
