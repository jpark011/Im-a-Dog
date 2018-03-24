package com.cs446w18.a16.imadog.model;

import java.util.HashMap;

public class Cat extends Player{
    public Cat(Game g) {
        super(g);
    }

    public String getRole() {
        return "CAT";
    }

    public String getQuestion() {
        int day = game.getCurrentDay();
        return game.getQuestion(false, day);
    }

    public void vote(String choice) {
        game.vote(this.getName(), choice);
    }

    public String getPollTitle() {
        if (game.isNight()) {
            return "Vote for the dog to kill";
        } else {
            return game.getQuestion(true, game.getCurrentDay());
        }
    }

    public HashMap<String, Integer> getVoteCount() {
        return game.getVoteCount();
    }
}
