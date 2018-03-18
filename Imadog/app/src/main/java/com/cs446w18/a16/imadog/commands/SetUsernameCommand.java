package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.Bluetooth;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class SetUsernameCommand implements Command, Serializable {
    private String clientName;
    private String username;
    private BluetoothServer receiver;

    public SetUsernameCommand(String clientName, String username) {
        this.clientName = clientName;
        this.username = username;
    }

    public void setReceiver(UserController user) {}
    public void setReceiver(PlayerController player) {}
    public void setReceiver(BluetoothServer server) {
        this.receiver = server;
    }

    public void execute() {
        this.receiver.setUserName(clientName, username);
    }
}
