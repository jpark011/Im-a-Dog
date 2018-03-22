package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

public class CloseDayPollCommand implements Command, Serializable {
    private String name;
    private String role;
    private String winner;
    private UserController receiver;

    public CloseDayPollCommand(String name, String role, String winner){
        this.name = name;
        this.role = role;
        this.winner = winner;
    }

    public void setReceiver(UserController user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.closePoll(name,role,winner);
    }
}
