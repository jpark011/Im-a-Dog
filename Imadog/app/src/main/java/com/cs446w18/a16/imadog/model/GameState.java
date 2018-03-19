package com.cs446w18.a16.imadog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lacie on 2018-03-19.
 */

public class GameState {
    private String role;
    private boolean inGame;
    private String gameFragment;
    private String currentQuestion;
    private String currentPollTitle;
    private HashMap<String, String> currentPollAnswers;
    private ArrayList<String> currentPollNames;
    private String currentVictimName;
    private String currentVictimRole;
    private int currentDuration;
    private Date currentStartTimer;

    public GameState(String role) {
        this.role = role;
        inGame = false;
    }

    public String getRole() {
        return role;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public String getGameFragment() {
        return gameFragment;
    }

    public void setGameFragment(String gameFragment) {
        this.gameFragment = gameFragment;
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(String currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String getCurrentPollTitle() {
        return currentPollTitle;
    }

    public void setCurrentPollTitle(String currentPollTitle) {
        this.currentPollTitle = currentPollTitle;
    }

    public HashMap<String, String> getCurrentPollAnswers() {
        return currentPollAnswers;
    }

    public void setCurrentPollAnswers(HashMap<String, String> currentPollAnswers) {
        this.currentPollAnswers = currentPollAnswers;
    }

    public ArrayList<String> getCurrentPollNames() {
        return currentPollNames;
    }

    public void setCurrentPollNames(ArrayList<String> currentPollNames) {
        this.currentPollNames = currentPollNames;
    }

    public String getCurrentVictimName() {
        return currentVictimName;
    }

    public void setCurrentVictimName(String currentVictimName) {
        this.currentVictimName = currentVictimName;
    }

    public String getCurrentVictimRole() {
        return currentVictimRole;
    }

    public void setCurrentVictimRole(String currentVictimRole) {
        this.currentVictimRole = currentVictimRole;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    public Date getCurrentStartTimer() {
        return currentStartTimer;
    }

    public void setCurrentStartTimer() {
        this.currentStartTimer = new Date();
    }
}
