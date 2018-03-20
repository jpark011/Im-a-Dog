package com.cs446w18.a16.imadog.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class ProfileActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */




    /* ----------------------------- METHODS ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        String role = Global.user.getRole();

        // Name label
        TextView nameLabel = findViewById(R.id.nameLabel);
        nameLabel.setText(Global.user.getUserName());

        // Role image view
        ImageView roleImageView = findViewById(R.id.roleImageView);
        roleImageView.setImageResource(role.equalsIgnoreCase("CAT") ? R.drawable.cat : R.drawable.dog);

        // Role label
        TextView roleLabel = findViewById(R.id.roleLabel);
        roleLabel.setText("A "+role+"!"); // KAREN: replace the cat string by the user's role

    }

}
