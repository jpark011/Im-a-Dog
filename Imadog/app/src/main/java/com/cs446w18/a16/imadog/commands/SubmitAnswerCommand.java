package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.presenter.PlayerPresenter;
import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.io.Serializable;

public class SubmitAnswerCommand implements Command, Serializable {
    private String answer;
    private PlayerPresenter receiver;

    public SubmitAnswerCommand(String answer) {
        this.answer = answer;
    }

    public void setReceiver(UserPresenter user) {}
    public void setReceiver(PlayerPresenter player) {
        this.receiver = player;
    }
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.receiver.submitAnswer(answer);
    }
}
