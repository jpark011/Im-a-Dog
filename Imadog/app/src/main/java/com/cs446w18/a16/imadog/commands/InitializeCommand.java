package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.controller.PlayerController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class InitializeCommand implements Command, Serializable {
    private String question;

    public InitializeCommand(String question) {
        this.question = question;
    }

    public void execute() {
        Global.user.initializeGame(question);
    }
    public void execute(PlayerController pc) {}
}
