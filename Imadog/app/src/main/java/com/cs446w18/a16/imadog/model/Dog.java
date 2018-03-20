package com.cs446w18.a16.imadog.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Dog extends Player {
    public Dog(Game g) {
        super(g);
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
            game.vote(this.getName(), choice);
        }
    }

    public String getNightPollTitle() {
        return "Vote for the best answer";
    }

    public HashMap<String, Integer> getNightVoteCount() {
        //return game.getPlayerNames(true, true, false);
        return game.getVoteCount();
    }
}
