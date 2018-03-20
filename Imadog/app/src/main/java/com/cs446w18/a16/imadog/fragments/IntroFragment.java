package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.model.GameConstants;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class IntroFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        String role = Global.user.getRole();

        // Role image view
        ImageView roleImageView = view.findViewById(R.id.roleImageView);
        roleImageView.setImageResource(role.equalsIgnoreCase("CAT") ? R.drawable.cat : R.drawable.dog);

        // Role label
        TextView roleLabel = view.findViewById(R.id.roleLabel);
        roleLabel.setText("A "+role+"!"); // KAREN: replace the cat string by the user's role

        // Delay the transition to the next page. To be replaced by an animation
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("ready to start view");
                Global.user.readyToStart();
            }
        }, 4000);


        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */


}
