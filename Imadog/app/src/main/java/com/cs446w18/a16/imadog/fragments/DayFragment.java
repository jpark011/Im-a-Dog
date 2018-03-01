package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.model.GameConstants;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class DayFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day, container, false);

        // TEST: delay the transition to the next page. To be replaced by a direct call by the model.
        if (GameConstants.INTERFACE_TEST) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getGameActivity().showQuestionPage(GameConstants.catQuestions[0]);
                }
            }, 2000);
        }

        // Set the name label
        TextView nameLabel = view.findViewById(R.id.titleLabel);
        String str;
        if (Global.user.getRole() == "DOG") {
            str = "There are cats lurking around.\nFind them and get rid of them!";
        } else {
            str = "Your mission is to go undercover and kill all the dogs.\nRemember to blend in!";
        }
        nameLabel.setText("It's the day!\nYou are a " + Global.user.getRole() + ". \n" + str);

        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */


}
