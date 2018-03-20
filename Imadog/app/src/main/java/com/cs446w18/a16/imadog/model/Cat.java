package com.cs446w18.a16.imadog.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Cat extends Player{
    public Cat(Game g) {
        super(g);
    }

    public ArrayList<String> getCatNames() {
        return this.game.getPlayerNames(true, false, false);
    }

    public ArrayList<String> getDogNames() {
        return this.game.getPlayerNames(false, true, false);
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

    public String getNightPollTitle() {
        return "Vote for the dog to kill";
    }

    public HashMap<String, Integer> getNightVoteCount() {
        return game.getVoteCount();
    }
}
