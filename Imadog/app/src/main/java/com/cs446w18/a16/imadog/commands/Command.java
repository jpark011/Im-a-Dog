package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.PlayerPresenter;
import com.cs446w18.a16.imadog.controller.UserPresenter;

/**
 * Created by JayP on 2018-03-11.
 */

public interface Command {
    void execute();
    void setReceiver(PlayerPresenter player);
    void setReceiver(UserPresenter user);
    void setReceiver(BluetoothServer server);
}
