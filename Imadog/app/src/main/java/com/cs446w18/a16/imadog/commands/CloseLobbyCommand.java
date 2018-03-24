package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.presenter.PlayerPresenter;
import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lacie on 2018-03-24.
 */

public class CloseLobbyCommand implements Command, Serializable {
    private UserPresenter user;

    public CloseLobbyCommand() {

    }

    public void setReceiver(UserPresenter user) {
        this.user = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.user.closeRoom();
    }
}
