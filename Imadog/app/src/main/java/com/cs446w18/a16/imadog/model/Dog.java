package com.cs446w18.a16.imadog.model;

import java.lang.reflect.Array;
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

    public HashMap<String, Integer> getVoteCount() {
        if (game.isNight()) {
            ArrayList<String> names = game.getPlayerNames(true, true, false);
            HashMap<String, Integer> count = new HashMap<>();
            for (int i = 0; i < names.size(); i++) {
                count.put(names.get(i), -1);
            }
            return count;
        }

        return game.getVoteCount();
    }
}
