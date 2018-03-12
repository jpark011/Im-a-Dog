package com.cs446w18.a16.imadog.activities;

import android.os.Bundle;
import android.widget.TextView;

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
        setContentView(R.layout.activity_profile);

        TextView titleLabel = findViewById(R.id.titleLabel);
        titleLabel.setText("You are a "+"cat"+"!"); // KAREN: replace the cat string by the user's role

    }

}
