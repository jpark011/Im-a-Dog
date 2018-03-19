package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

public class SendMessageCommand implements Command, Serializable {
    private String text;
    private PlayerController receiver;

    public SendMessageCommand(String text) {
        this.text = text;
    }

    public void setReceiver(UserController user) {}
    public void setReceiver(PlayerController player) {
        this.receiver = player;
    }
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.sendMessage(text);
    }
}
