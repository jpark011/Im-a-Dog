package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    /* ----------------------------- METHODS ----------------------------- */


}
