package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.controller.PlayerController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class SetUsernameCommand implements Command, Serializable {
    private String name;

    public SetUsernameCommand(String name) {
        this.name = name;
    }

    public void execute() {}
    public void execute(PlayerController pc) {
        pc.setUserName(name);
    }
}
