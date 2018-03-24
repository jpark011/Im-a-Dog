package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.presenter.PlayerPresenter;
import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-24.
 */

public class LeaveLobbyCommand implements Command, Serializable {
    private String clientName;
    private BluetoothServer receiver;

    public LeaveLobbyCommand(String clientName) {
        this.clientName = clientName;
    }

    public void setReceiver(UserPresenter user) {}
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {
        this.receiver = server;
    }

    public void execute() {
        this.receiver.removeMember(clientName);
    }
}
