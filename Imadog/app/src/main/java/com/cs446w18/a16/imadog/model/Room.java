package com.cs446w18.a16.imadog.model;

import android.bluetooth.BluetoothAdapter;

import com.cs446w18.a16.imadog.controller.GameController;
import com.cs446w18.a16.imadog.controller.User;
import com.cs446w18.a16.imadog.controller.UserClient;
import com.cs446w18.a16.imadog.controller.UserHost;
import com.cs446w18.a16.imadog.services.ServerThread;

import java.util.ArrayList;

/**
 * The waiting room.
 */

public class Room {
    private UserHost host;
    private ArrayList<UserHost> members;
    private ServerThread thread;

    public Room(BluetoothAdapter btAdapter, String MY_UUID) {
        members = new ArrayList<>();
        thread = new ServerThread(btAdapter, MY_UUID, this);
        thread.start();
    }

    public void setHost(UserHost host) {
        this.host = host;
    }

    public void addMember(UserHost member) {
//        if (members.contains(member)) return false;
        members.add(member);
//        return true;
    }

    public void removeMember(UserHost member) {
        members.remove(member);
    }

    public ArrayList<UserHost> getMembers() {
        return members;
    }

    public int getSize() {
        return members.size();
    }

    public UserHost startGame() {
        thread.stopAccepting();
        host.setGame(new GameController(members));
        return host;
    }
}
