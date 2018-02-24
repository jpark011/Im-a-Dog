package com.cs446w18.a16.imadog.model;

import com.cs446w18.a16.imadog.controller.User;

import java.util.ArrayList;

/**
 * The waiting room.
 */

public class Room {
    private User host;
    private ArrayList<User> members;

    public Room(User host) {
        this.host = host;
        members = new ArrayList<>();
        members.add(host);
    }

    public boolean addMember(User member) {
        if (members.contains(member)) return false;
        members.add(member);
        return true;
    }

    public void removeMember(User member) {
        members.remove(member);
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public int getSize() {
        return members.size();
    }

    public Game startGame(User user) {
        if (isHost(user)) {
            //if (members.size() < GameConstants.minPlayers) return null;
            return new Game(members);
        }

        return null;
    }

    public boolean isHost(User user) {
        return user.getUserName() == host.getUserName();
    }
}
