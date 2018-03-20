package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;
import java.util.HashMap;

public class StartDayPollCommand implements Command, Serializable {
    private String question;
    private HashMap<String, String> answers;
    private HashMap<String, Integer> votes;
    private UserController receiver;

    public StartDayPollCommand(String question, HashMap<String,String> answers, HashMap<String, Integer> votes) {
        this.question = question;
        this.answers = answers;
        this.votes = votes;
    }

    public void setReceiver(UserController user) {
        this.receiver = user;
    }
    public void setReceiver(PlayerController player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.startPoll(question, answers, votes);
    }
}
