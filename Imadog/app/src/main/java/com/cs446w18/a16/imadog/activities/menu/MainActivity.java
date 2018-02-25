package com.cs446w18.a16.imadog.activities.menu;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.HelpActivity;
import com.cs446w18.a16.imadog.activities.SuperActivity;

public class MainActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    private BluetoothAdapter mBluetoothAdapter;
    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();

        int REQUEST_ENABLE_BT = 3;
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
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
