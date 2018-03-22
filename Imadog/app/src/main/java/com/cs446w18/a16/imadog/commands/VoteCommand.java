package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.presenter.PlayerPresenter;
import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.io.Serializable;

public class VoteCommand implements Command, Serializable {
    private String choice;
    private PlayerPresenter receiver;

    public VoteCommand(String choice) {
        this.choice = choice;
    }

    public void setReceiver(UserPresenter user) {}
    public void setReceiver(PlayerPresenter player) {
        this.receiver = player;
    }
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.vote(choice);
    }
}
