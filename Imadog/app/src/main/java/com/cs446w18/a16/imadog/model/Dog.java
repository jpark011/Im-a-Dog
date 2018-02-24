package com.cs446w18.a16.imadog.model;

/**
 * Created by lacie on 2018-02-16.
 */

public class Dog extends Player {
    public Dog(String n, Game g) {
        super(n, g);
    }

    public String getRole() {
        return "DOG";
    }

    public String getQuestion() {
        int day = game.getCurrentDay();
        return game.getQuestion(true, day);
    }

    public void vote(String choice) {
        if (!game.isNight()) {
            game.vote(this.name, choice);
        }
    }
}
