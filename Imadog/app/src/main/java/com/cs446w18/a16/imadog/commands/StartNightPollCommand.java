package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.controller.PlayerController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lacie on 2018-03-18.
 */

public class StartNightPollCommand implements Command, Serializable {
    private String title;
    private ArrayList<String> names;

    public StartNightPollCommand(String title, ArrayList<String> names) {
        this.title = title;
        this.names = names;
    }

    public void execute() {
        Global.user.startNightPoll(title, names);
    }
    public void execute(PlayerController pc) {}
}
