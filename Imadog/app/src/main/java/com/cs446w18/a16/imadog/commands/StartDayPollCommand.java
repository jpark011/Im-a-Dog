package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.controller.PlayerController;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lacie on 2018-03-18.
 */

public class StartDayPollCommand implements Command, Serializable {
    private String question;
    private HashMap<String, String> answers;

    public StartDayPollCommand(String question, HashMap<String,String> answers) {
        this.question = question;
        this.answers = answers;
    }

    public void execute() {
        Global.user.startPoll(question, answers);
    }
    public void execute(PlayerController pc) {}
}
