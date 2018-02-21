package com.cs446w18.a16.imadog.fragments;

import android.support.v4.app.Fragment;

import com.cs446w18.a16.imadog.activities.GameActivity;

/**
 * Created by Jean-Baptiste on 20/02/2018.
 */

public class SuperFragment extends Fragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- METHODS ----------------------------- */

    /// Gets the game activity
    protected GameActivity getGameActivity() {
        return (GameActivity) getActivity();
    }

}
