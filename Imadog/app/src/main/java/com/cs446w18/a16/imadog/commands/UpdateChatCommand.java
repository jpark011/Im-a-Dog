package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.presenter.PlayerPresenter;
import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateChatCommand implements Command, Serializable {
    private ArrayList<String> names;
    private ArrayList<String> messages;
    private UserPresenter receiver;

    public UpdateChatCommand(ArrayList<String> names, ArrayList<String> messages) {
        this.names = names;
        this.messages = messages;
    }

    public void setReceiver(UserPresenter user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.updateChat(names, messages);
    }
}
