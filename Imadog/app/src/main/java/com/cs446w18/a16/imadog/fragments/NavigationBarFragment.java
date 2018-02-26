package com.cs446w18.a16.imadog.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class NavigationBarFragment extends Fragment {

    public interface Delegate {
        void leftBarButtonWasPressed();
        void rightBarButtonWasPressed();
    }

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    Button leftButton, rightButton;

    Delegate delegate;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);

        // Left button
        leftButton = view.findViewById(R.id.leftButton);
        leftButton.setText("Help");

        // Right button
        rightButton = view.findViewById(R.id.rightButton);
        rightButton.setText("Profile");

        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the left button is pressed
    public void leftButton(View view) {
        delegate.leftBarButtonWasPressed();
    }

    /// CALLBACK: when the right button is pressed
    public void rightButton(View view) {
        delegate.rightBarButtonWasPressed();
    }



}
