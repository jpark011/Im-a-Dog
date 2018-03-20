package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.model.GameConstants;
import com.cs446w18.a16.imadog.views.VoteListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class VoteFragment extends SuperFragment implements VoteListView.Delegate {

    public interface Delegate {
        void changedVoteFor(String playerName);
    }

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    TextView questionLabel;
    TextView timerLabel;

    VoteListView playersListView;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vote, container, false);
        Bundle bundle = getArguments();

        boolean isNightVote = bundle.getBoolean("isNight");

        // Set the question label
        questionLabel = view.findViewById(R.id.titleLabel);
        questionLabel.setText(bundle.getString("title"));

        // Set the subtitle label
        TextView subtitleLabel = view.findViewById(R.id.subtitleLabel);
        subtitleLabel.setText(isNightVote ? "It’s time for the night vote" : "The dog question was");

        // Set the people/answers list
        Map<String, Integer> votes = (Map<String, Integer>) bundle.getSerializable("votes");

        playersListView = view.findViewById(R.id.voteListView);
        if (isNightVote) {
            playersListView.setup(votes, this);
        }
        else {
            Map<String, String> answers = (Map<String, String>) bundle.getSerializable("answers");
            playersListView.setup(votes, answers, this);
        }

        // Lock the view if player is dead
        if (Global.user.isDead()) {
            playersListView.lock();
        }

        // Set the countdown timer
        long duration = isNightVote ? GameConstants.nightVoteDuration : GameConstants.dayVoteDuration;
        timerLabel = view.findViewById(R.id.timerLabel);
        setTimerLabel(duration);


        CountDownTimer timer = new CountDownTimer(duration*1000, 1000) {
            @Override
            public void onTick(long l) {
                setTimerLabel(l);
            }

            @Override
            public void onFinish() {
                // TEST: to be replaced by a direct call from the model.
//                if (GameConstants.INTERFACE_TEST) {
//                    getGameActivity().showVictimPage("Player1", "Cat");
//                }

                // TODO: show "Waiting for results..."
            }
        }.start();

        return view;
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// Updates the timer label with the given time
    private void setTimerLabel(long time) {
        int minutes = (int) time / 60000;
        int seconds = (int) time % 60000 / 1000;
        timerLabel.setText(minutes + ":" + String.format("%02d", seconds));
    }

    @Override
    public void playerWasSelected(String name) {
        System.out.println("Voted for "+name);
        getGameActivity().changedVoteFor(name);
    }
}
