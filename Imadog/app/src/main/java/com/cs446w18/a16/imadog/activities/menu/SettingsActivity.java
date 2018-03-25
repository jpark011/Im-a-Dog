package com.cs446w18.a16.imadog.activities.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class SettingsActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */




    /* ----------------------------- METHODS ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Page background
        changeBackgroudColor(R.color.light_red);

    }

}
