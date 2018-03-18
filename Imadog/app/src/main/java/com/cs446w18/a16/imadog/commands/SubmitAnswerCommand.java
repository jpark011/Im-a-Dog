package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.controller.PlayerController;

import java.io.Serializable;

/**
 * Created by lacie on 2018-03-18.
 */

public class SubmitAnswerCommand implements Command, Serializable {
    private String answer;

    public SubmitAnswerCommand(String answer) {
        this.answer = answer;
    }

    public void execute(){}
    public void execute(PlayerController pc) {
        pc.submitAnswer(answer);
    }
}
