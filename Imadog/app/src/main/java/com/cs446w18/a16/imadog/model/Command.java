package com.cs446w18.a16.imadog.model;

import java.util.ArrayList;

/**
 * Created by Lacie Yi on 2018-02-25.
 */

public class Command {
    private String command;
    private ArrayList<Object> args;

    public Command(String command, ArrayList<Object> args) {
        this.command = command;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<Object> getArgs() {
        return args;
    }
}
