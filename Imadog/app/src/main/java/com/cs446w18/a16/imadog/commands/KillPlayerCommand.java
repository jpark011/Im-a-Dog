package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-19.
 */

public class KillPlayerCommand implements Command, Serializable {
    private UserController receiver;

    public KillPlayerCommand() {}

    public void setReceiver(UserController user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.setDead();
    }
}
