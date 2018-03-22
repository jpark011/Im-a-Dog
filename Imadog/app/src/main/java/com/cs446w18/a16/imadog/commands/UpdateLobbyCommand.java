package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserPresenter;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateLobbyCommand implements Command, Serializable {
    private ArrayList<String> members;
    private UserPresenter user;

    public UpdateLobbyCommand(ArrayList<String> members) {
        this.members = members;
    }

    public void setReceiver(UserPresenter user) {
        this.user = user;
    }
    public void setReceiver(PlayerPresenter player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.user.updateRoomMembers(members);
    }
}
