package com.cs446w18.a16.imadog.model;

/**
 * Created by lacie on 2018-03-19.
 */

public class Message {
    String text;
    String name;

    public Message(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }
}
