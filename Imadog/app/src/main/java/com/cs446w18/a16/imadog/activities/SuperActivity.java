package com.cs446w18.a16.imadog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.menu.CreateGameActivity;
import com.cs446w18.a16.imadog.activities.menu.LobbyActivity;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class SuperActivity extends AppCompatActivity {


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        hideSystemUI();


        // Background
        getWindow().getDecorView().setBackgroundResource(R.drawable.background);

    }


    /* ----------------------------- METHODS ----------------------------- */

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /// CALLBACK
    public void back(View view) {
        hideSystemUI();
        finish();
    }


}
