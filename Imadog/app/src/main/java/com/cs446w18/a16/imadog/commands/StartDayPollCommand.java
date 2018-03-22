package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserPresenter;

import java.io.Serializable;
import java.util.HashMap;

public class StartDayPollCommand implements Command, Serializable {
    private String question;
    private HashMap<String, String> answers;
    private HashMap<String, Integer> votes;
    private UserPresenter receiver;

    public StartDayPollCommand(String question, HashMap<String,String> answers, HashMap<String, Integer> votes) {
        this.question = question;
        this.answers = answers;
        this.votes = votes;
    }

    public void setReceiver(UserPresenter user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.startPoll(question, answers, votes);
    }
}
