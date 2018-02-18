package com.cs446w18.a16.imadog.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.HelpActivity;
import com.cs446w18.a16.imadog.activities.SuperActivity;

public class MainActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    /// The buttons of the menu
    private Button joinButton, createButton, settingsButton;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Join button
        joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent joinGameIntent = new Intent(MainActivity.this, JoinGameActivity.class);
                startActivity(joinGameIntent);
            }
        });

        // Create game button
        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createGameIntent = new Intent(MainActivity.this, CreateGameActivity.class);
                startActivity(createGameIntent);
            }
        });

        // Settings button
        settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    /* ----------------------------- METHODS ----------------------------- */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Help button
        if (item.getItemId() == R.id.help_action) {

            Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(helpIntent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
