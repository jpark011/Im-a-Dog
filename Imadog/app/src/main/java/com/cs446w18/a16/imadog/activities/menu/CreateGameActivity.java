package com.cs446w18.a16.imadog.activities.menu;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.services.ServerThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class CreateGameActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */
    /**
     * Tag for Log
     */
    private static final String TAG = "JoinGameActivity";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;

    private final int BT_DISCOVERABLE = 3;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == BT_DISCOVERABLE && resultCode != RESULT_CANCELED) {
            // Move forward to Lobby
            Intent joinIntent = new Intent(CreateGameActivity.this, LobbyActivity.class);
            startActivity(joinIntent);
        }
    }

    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Create button is pressed
    public void createGame(View view) {
        Global.user.createGame(mBtAdapter, getResources().getText(R.string.UUID).toString());

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
        startActivityForResult(discoverableIntent, BT_DISCOVERABLE);
    }

}
