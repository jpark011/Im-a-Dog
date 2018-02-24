package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


        // Delay the transition to the next page. To be replaced by an animation
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (GameConstants.INTERFACE_TEST) {
                    getGameActivity().showDayPage();
                } else {
                    System.out.println("ready to start view");
                    Global.user.readyToStart();
                }
            }
        }, 2000);


        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    /* ----------------------------- METHODS ----------------------------- */


}