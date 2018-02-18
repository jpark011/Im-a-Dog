package com.cs446w18.a16.imadog.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class OutroFragment extends Fragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_outro, container, false);
    }

    /* ----------------------------- METHODS ----------------------------- */


}
