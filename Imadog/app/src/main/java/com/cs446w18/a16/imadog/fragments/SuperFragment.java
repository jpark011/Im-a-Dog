package com.cs446w18.a16.imadog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
