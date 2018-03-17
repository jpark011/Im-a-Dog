package com.cs446w18.a16.imadog.model;

import java.util.ArrayList;

/**
 * A player of the game, either a cat or dog
 */

public abstract class Player {
    private String name;
    private boolean dead;
    protected Game game;
    private ArrayList<String> answers;

    public Player(Game g) {
        answers = new ArrayList<>();
        game = g;
        dead = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("ROLE SET NAME: " + name);
    }

    public String getAnswer(int day) {
        if (answers.size() < day) {
            answers.add("");
        }
        return answers.get(day-1);
    }

    public void setAnswer(String ans) {
        int day = game.getCurrentDay();
        if (answers.size() < day) {
            answers.add(ans);
        }
        System.out.println("SET ANSWER: " + name + " : " + ans);

        answers.set(day-1, ans);
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
