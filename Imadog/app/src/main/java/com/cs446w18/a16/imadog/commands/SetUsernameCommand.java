package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class SetUsernameCommand implements Command, Serializable {
    private String name;
    private PlayerController receiver;

    public SetUsernameCommand(String name) {
        this.name = name;
    }

    public void setReceiver(UserController user) {}
    public void setReceiver(PlayerController player) {
        this.receiver = player;
    }

    public void execute() {
        this.receiver.setUserName(name);
    }
}
