package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 2018-03-20.
 */

public class ProfileFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String role = Global.user.getRole();

        // Name label
        TextView nameLabel = view.findViewById(R.id.nameLabel);
        nameLabel.setText(Global.user.getUserName());

        // Role image view
        ImageView roleImageView = view.findViewById(R.id.roleImageView);
        roleImageView.setImageResource(role.equalsIgnoreCase("CAT") ? R.drawable.cat : R.drawable.dog);

        // Role label
        TextView roleLabel = view.findViewById(R.id.roleLabel);
        roleLabel.setText("A "+role+"!"); // KAREN: replace the cat string by the user's role


        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */


}
