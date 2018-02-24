package com.cs446w18.a16.imadog.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.HelpActivity;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.controller.User;

public class MainActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Global.user == null) {
            Global.user = new User("SomeName");
        }
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Join button is pressed
    public void joinGame(View view) {
        Intent joinGameIntent = new Intent(MainActivity.this, JoinGameActivity.class);
        startActivity(joinGameIntent);
    }

    /// CALLBACK: when the Create button is pressed
    public void createGame(View view) {
        Intent createGameIntent = new Intent(MainActivity.this, CreateGameActivity.class);
        startActivity(createGameIntent);
    }

    /// CALLBACK: when the Settings button is pressed
    public void showSettings(View view) {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


}
