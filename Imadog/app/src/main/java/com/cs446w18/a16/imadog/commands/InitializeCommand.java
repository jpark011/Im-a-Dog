package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class InitializeCommand implements Command, Serializable {
    private String question;
    private UserController receiver;

    public InitializeCommand(String question) {
        this.question = question;
        this.receiver = null;
    }

    public void setReceiver(UserController user) {
        this.receiver = user;
    }

    public void setReceiver(PlayerController player) {}

    public void execute() {
        this.receiver.initializeGame(question);
    }
}
