package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserPresenter;

import java.io.Serializable;

public class InitializeCommand implements Command, Serializable {
    private String role;
    private String question;
    private UserPresenter receiver;

    public InitializeCommand(String question, String role) {
        this.role = role;
        this.question = question;
        this.receiver = null;
    }

    public void setReceiver(UserPresenter user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.initializeGame(question, role);
    }
}
