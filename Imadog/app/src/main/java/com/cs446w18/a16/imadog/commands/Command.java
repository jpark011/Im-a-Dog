package com.cs446w18.a16.imadog.commands;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JayP on 2018-03-11.
 */

public class Command implements Serializable {
    private String action;
    private Object[] payload;

    public Command(String action, Object[] payload) {
        this.action = action;
        this.payload = payload;
    }

    public String getAction() {
        return action;
    }

    public Object[] getPayload() {
        return payload;
    }
}
