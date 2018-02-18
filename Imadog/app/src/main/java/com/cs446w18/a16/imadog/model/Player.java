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
    private Poll poll;

    public Player(String n, Game g) {
        answers = new ArrayList<>();
        poll = null;
        name = n;
        game = g;
        dead = false;
    }

    public String getName() {
        return name;
    }

    public String getAnswer(int day) {
        return answers.get(day-1);
    }

    public void setAnswer(String ans) {
        answers.add(ans);
    }

    public ArrayList<String> getCurrentPlayerNames() {
        return game.getPlayerNames(true, true, false);
    }

    public void setPoll(Poll p) {
        poll = p;
    }

    public void clearPoll() {
        poll = null;
    }

    public void vote(String n) {
        poll.setVote(name, n);
    }

    public void kill() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    abstract public String getQuestion();
}
