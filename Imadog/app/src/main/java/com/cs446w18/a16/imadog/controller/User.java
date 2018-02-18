package com.cs446w18.a16.imadog.controller;

import com.cs446w18.a16.imadog.model.Game;
import com.cs446w18.a16.imadog.model.Player;
import com.cs446w18.a16.imadog.model.Room;

/**
 * Created by lacie on 2018-02-18.
 */

public class User {
    private String userName;
    Room room;
    Game game;
    Player role;

    public User(String name) {
        userName = name;
        room = null;
        game = null;
        role = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public void joinRoom(Room room) {
        boolean result = room.addMember(this);
        if (result) {
            this.room = room;
        }
    }

    public void leaveRoom() {
        this.room.removeMember(this);
    }

    public void createGame() {
        this.room.createGame(this);
    }

    public void startGame(Game game) {
        this.game = game;
    }

    public void setRole(Player role) {
        this.role = role;
    }

    public Player getRole() {
        return role;
    }

    public void submitAnswer(String answer) {
        role.setAnswer(answer);
    }

    public String getQuestion() {
        return role.getQuestion();
    }
}
