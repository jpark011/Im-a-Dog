package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserPresenter;

import java.io.Serializable;

public class SendMessageCommand implements Command, Serializable {
    private String text;
    private PlayerPresenter receiver;

    public SendMessageCommand(String text) {
        this.text = text;
    }

    public void setReceiver(UserPresenter user) {}
    public void setReceiver(PlayerPresenter player) {
        this.receiver = player;
    }
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.sendMessage(text);
    }
}
