package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserController;
import com.cs446w18.a16.imadog.model.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lacie on 2018-03-19.
 */

public class UpdateChatCommand implements Command, Serializable {
    private ArrayList<Message> history;
    private UserController receiver;

    public UpdateChatCommand(ArrayList<Message> history) {
        this.history = history;
    }

    public void setReceiver(UserController user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.updateChat(history);
    }
}
