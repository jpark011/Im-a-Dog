package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.model.GameConstants;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class VictimFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    TextView nameLabel, roleLabel;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_victim, container, false);
        Bundle bundle = getArguments();

        // Set the name label
        nameLabel = view.findViewById(R.id.nameLabel);
        nameLabel.setText(bundle.getString("victimName")+"\nhas been killed");

        // Set the role label
        roleLabel = view.findViewById(R.id.roleLabel);
        roleLabel.setText("He/she was...\nA "+bundle.getString("victimRole")+"!");

        if (GameConstants.INTERFACE_TEST) {
            // Delay the transition to the next page. To be replaced by an animation
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getGameActivity().showNightPage();
                }
            }, 5000);
        }

        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */


}
