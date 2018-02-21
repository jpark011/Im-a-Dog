package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.model.GameConstants;

import java.util.ArrayList;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class NightFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // TEST: delay the transition to the next page. To be replaced by a direct call by the model.
        if (GameConstants.INTERFACE_TEST) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> players = new ArrayList<String>();
                    players.add("Alice");
                    players.add("Bob");
                    players.add("Carol");
                    getGameActivity().showNightVotePage("Vote to kill a dog...", players);
                }
            }, 2000);
        }

        return inflater.inflate(R.layout.fragment_night, container, false);
    }

    /* ----------------------------- METHODS ----------------------------- */


}
