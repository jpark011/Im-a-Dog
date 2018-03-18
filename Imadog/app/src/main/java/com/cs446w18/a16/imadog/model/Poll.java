package com.cs446w18.a16.imadog.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by lacie on 2018-02-17.
 */

public class Poll {
    private Map<String, String> votes;
    private Map<String, Integer> count;
    private boolean isOpen;

    public Poll(ArrayList<String> names, ArrayList<String> choices) {
        isOpen = true;
        votes = new HashMap();
        count = new HashMap();
        for (int i = 0; i < names.size(); i++) {
            votes.put(names.get(i), null);
        }

        for (int i = 0; i < choices.size(); i++) {
            count.put(choices.get(i), 0);
        }
    }

    public String getUserVote(String userName) {
        return votes.get(userName);
    }

    public void setVote(String userName, String voteName) {
        if (!isOpen) return;

        if (votes.get(userName) != null) {
            String oldVote = votes.get(userName);
            int oldCount = count.get(oldVote);
            count.put(oldVote, oldCount-1);
        }

        int newCount = count.get(voteName);
        count.put(voteName, newCount+1);
        votes.put(userName, voteName);
    }

    public int getVoteCount(String userName) {
        return count.get(userName);
    }

    public String getResult() {
        ArrayList<String> maxNames = new ArrayList<String>();
        int maxVotes = 0;
        for (Map.Entry<String, Integer> vote : count.entrySet()) {
            if (vote.getValue() > maxVotes) {
                maxVotes = vote.getValue();
                maxNames.clear();
                maxNames.add(vote.getKey());
            } else if (vote.getValue() == maxVotes) {
                maxNames.add(vote.getKey());
            }
        }

        if (maxNames.size() == 1 && maxVotes > 0) return maxNames.get(0);
        return null;
    }

    public String closePoll() {
        isOpen = false;
        return getResult();
    }
}
