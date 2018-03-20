package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 2018-03-20.
 */

public class HelpFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, container, false);


        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */


}
