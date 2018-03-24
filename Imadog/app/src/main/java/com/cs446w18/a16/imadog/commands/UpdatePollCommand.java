package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.presenter.PlayerPresenter;
import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lacie on 2018-03-23.
 */

public class UpdatePollCommand implements Command, Serializable {
    private HashMap<String, Integer> votes;
    private UserPresenter receiver;

    public UpdatePollCommand(HashMap<String, Integer> votes) {
        this.votes = votes;
    }

    public void setReceiver(UserPresenter user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.updatePoll(votes);
    }
}
