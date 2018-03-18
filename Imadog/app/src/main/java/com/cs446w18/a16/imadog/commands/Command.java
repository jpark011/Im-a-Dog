package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerController;
import com.cs446w18.a16.imadog.controller.UserController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JayP on 2018-03-11.
 */

public interface Command {
    void execute();
    void setReceiver(PlayerController player);
    void setReceiver(UserController user);
    void setReceiver(BluetoothServer server);
}
