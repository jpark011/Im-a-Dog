package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class NavigationBarFragment extends Fragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_bar, container, false);
    }


    /* ----------------------------- METHODS ----------------------------- */


}
