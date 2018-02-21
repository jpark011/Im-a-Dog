package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.model.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class VoteFragment extends SuperFragment {

    public interface Delegate {
        void changedVoteFor(String playerName);
    }

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    TextView questionLabel;
    TextView timerLabel;


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

        // Set the people/answers list
        final String firstPlayer;
        if (isNightVote) {
            List<String> players = bundle.getStringArrayList("players");
            firstPlayer = players.get(0);
        }
        else {
            Map<String, String> answers = (Map<String, String>) bundle.getSerializable("answers");
            List<String> players = new ArrayList<String>(answers.keySet());
            firstPlayer = players.get(0);
        }

        // TEST: make a dummy vote for a player (the first) after 2 seconds.
        if (GameConstants.INTERFACE_TEST) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getGameActivity().changedVoteFor(firstPlayer);
                }
            }, 2000);
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
                if (GameConstants.INTERFACE_TEST) {
                    getGameActivity().showVictimPage("Player1", "Cat");
                }

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


}
