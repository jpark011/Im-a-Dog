package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lacie on 2018-03-18.
 */

public class StartNightPollCommand implements Command, Serializable {
    private String title;
    private ArrayList<String> names;
    private UserController receiver;

    public StartNightPollCommand(String title, ArrayList<String> names) {
        this.title = title;
        this.names = names;
    }

    public void setReceiver(UserController user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerController player) {}

    public void execute() {
        this.receiver.startNightPoll(title, names);
    }
}
