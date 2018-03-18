package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateLobbyCommand implements Command, Serializable {
    private ArrayList<String> members;
    private UserController user;

    public UpdateLobbyCommand(ArrayList<String> members) {
        this.members = members;
    }

    public void setReceiver(UserController user) {
        this.user = user;
    }
    public void setReceiver(PlayerController player) {}
    public void setReceiver(BluetoothServer server) {}

    public void execute() {
        this.user.updateRoomMembers(members);
    }
}
