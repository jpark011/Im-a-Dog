package com.cs446w18.a16.imadog.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.model.GameConstants;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class VictimFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    TextView nameLabel, roleLabel;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_victim, container, false);

        // Get arguments
        Bundle bundle = getArguments();
        String name = bundle.getString("victimName");
        String role = bundle.getString("victimRole");


        // Set the name label
        nameLabel = view.findViewById(R.id.nameLabel);
        nameLabel.setText(name);

        // Role image view
        ImageView roleImageView = view.findViewById(R.id.roleImageView);
        roleImageView.setImageResource(role.equalsIgnoreCase("CAT") ? R.drawable.cat : R.drawable.dog);

        // Set the role label
        roleLabel = view.findViewById(R.id.roleLabel);
        roleLabel.setText("A "+role+"!");

        // Case where nobody was killed
        if (name == null) {
            nameLabel.setText("Nobody\nwas killed");
            roleLabel.setText("");
        }


        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */


}
