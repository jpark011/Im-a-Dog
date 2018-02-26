package com.cs446w18.a16.imadog.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class OutroFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    TextView titleLabel;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_outro, container, false);
        Bundle bundle = getArguments();

        // Delay the transition to the next page. To be replaced by an animation
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGameActivity().finishGame();
            }
        }, 2000);

        // Set the title label
        String winner = bundle.getString("winner");
        titleLabel = view.findViewById(R.id.titleLabel);
        titleLabel.setText("The "+winner+" won!");

        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */


}
