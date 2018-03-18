package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class CloseNightPollCommand implements Command, Serializable {
    private String name;
    private String role;
    private String winner;
    private String question;
    private UserController receiver;

    public CloseNightPollCommand(String name,  String role, String winner, String question) {
        this.name = name;
        this.role = role;
        this.winner = winner;
        this.question = question;
    }

    public void setReceiver(UserController user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerController player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.closeNightPoll(name, role, winner, question);
    }
}
