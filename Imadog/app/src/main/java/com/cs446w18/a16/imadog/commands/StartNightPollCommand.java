package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StartNightPollCommand implements Command, Serializable {
    private String title;
    private HashMap<String, Integer> votes;
    private UserController receiver;

    public StartNightPollCommand(String title, HashMap<String, Integer> votes) {
        this.title = title;
        this.votes = votes;
    }

    public void setReceiver(UserController user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerController player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.startNightPoll(title, votes);
    }
}
