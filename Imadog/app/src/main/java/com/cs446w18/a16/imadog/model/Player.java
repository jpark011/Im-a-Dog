package com.cs446w18.a16.imadog.model;

import java.util.ArrayList;

/**
 * A player of the game, either a cat or dog
 */

public abstract class Player {
    protected String name;
    private boolean dead;
    protected Game game;
    private ArrayList<String> answers;

    public Player(String n, Game g) {
        answers = new ArrayList<>();
        name = n;
        game = g;
        dead = false;
    }

    public String getName() {
        return name;
    }

    public String getAnswer(int day) {
        if (answers.size() == day-1) {
            answers.add("");
        }
        return answers.get(day-1);
    }

    public void setAnswer(String ans) {
        int day = game.getCurrentDay();
        if (answers.size() < day) {
            answers.add(ans);
        }

        answers.add(day-1, ans);
    }

    public void kill() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    abstract public String getQuestion();

    abstract public void vote(String choice);

    abstract  public String getRole();
}
