package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.controller.PlayerController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class CloseDayPollCommand implements Command, Serializable {
    private String name;
    private String role;
    private String winner;

    public CloseDayPollCommand(String name, String role, String winner){
        this.name = name;
        this.role = role;
        this.winner = winner;
    }

    public void execute() {
        Global.user.closePoll(name,role,winner);
    }
    public void execute(PlayerController pc) {}
}
