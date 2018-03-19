package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

public class SubmitAnswerCommand implements Command, Serializable {
    private String answer;
    private PlayerController receiver;

    public SubmitAnswerCommand(String answer) {
        this.answer = answer;
    }

    public void setReceiver(UserController user) {}
    public void setReceiver(PlayerController player) {
        this.receiver = player;
    }
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.submitAnswer(answer);
    }
}