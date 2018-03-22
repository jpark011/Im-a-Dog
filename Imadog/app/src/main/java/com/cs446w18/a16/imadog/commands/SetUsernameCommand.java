package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.presenter.PlayerPresenter;
import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.io.Serializable;

public class SetUsernameCommand implements Command, Serializable {
    private String clientName;
    private String username;
    private BluetoothServer receiver;

    public SetUsernameCommand(String clientName, String username) {
        this.clientName = clientName;
        this.username = username;
    }

    public void setReceiver(UserPresenter user) {}
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {
        this.receiver = server;
    }

    public void execute() {
        this.receiver.setUserName(clientName, username);
    }
}
