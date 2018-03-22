package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

public class SetClientNameCommand implements Command, Serializable {
    private String clientName;
    private UserController user;

    public SetClientNameCommand(String clientName) {
        this.clientName = clientName;
    }

    public void setReceiver(UserController user) {
        this.user = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        user.setClientName(clientName);
    }
}
