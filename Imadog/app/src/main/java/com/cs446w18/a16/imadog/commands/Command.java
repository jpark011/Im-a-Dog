package com.cs446w18.a16.imadog.commands;

import com.cs446w18.a16.imadog.controller.PlayerController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JayP on 2018-03-11.
 */

public interface Command {
    void execute();
    void execute(PlayerController pc);
}
