package com.cs446w18.a16.imadog.model;

import com.cs446w18.a16.imadog.presenter.PlayerPresenter;

import java.util.ArrayList;

/**
 * Created by lacie on 2018-03-19.
 */

public class Chat {
    ArrayList<PlayerPresenter> members;
    ArrayList<Message> messages;

    public Chat(ArrayList<PlayerPresenter> members) {
        this.members = new ArrayList<>(members);
        messages = new ArrayList<>();
    }

    public void addMessage(String text, String name) {
        Message m = new Message(text, name);
        messages.add(m);
        for (int i = 0; i < members.size(); i++) {
            final PlayerPresenter member = members.get(i);
            new Thread() {
                public void run() {
                    member.updateChat();
                }
            }.start();
        }
    }

    public ArrayList<PlayerPresenter> getMembers() {
        return members;
    }

    public ArrayList<Message> getMessages() {
        return new ArrayList<>(messages);
    }
}
