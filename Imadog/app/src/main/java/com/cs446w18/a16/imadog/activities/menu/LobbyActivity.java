package com.cs446w18.a16.imadog.activities.menu;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.commands.Command;
import com.cs446w18.a16.imadog.views.VoteListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class LobbyActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */
    Toast mToast;
    List<String> mPlayers;

    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Players list
        VoteListView playersList = findViewById(R.id.playersListView);
        // TODO: Replace with real players
        ArrayList<String> testList = new ArrayList<>();
        testList.add("Alice");
        testList.add("Bob");
        testList.add("Carol");
        playersList.setup(testList, null);
        playersList.isEnabled = false;

        mPlayers = new ArrayList<>();
        if (Global.user.isServer()) {
            Global.user.getServer().setCommunicationCallback(new CommunicationCallbackServer());
        } else {
            Global.user.getClient().setCommunicationCallback(new CommunicationCallbackClient());
        }
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Start button is pressed
    public void startGame(View view) {
        Global.user.startGame();
        Intent startGameIntent = new Intent(LobbyActivity.this, GameActivity.class);
        startActivity(startGameIntent);
    }

    private class CommunicationCallbackServer implements CommunicationCallback {
        @Override
        public void onConnect(BluetoothDevice device) {
        }

        @Override
        public void onDisconnect(BluetoothDevice device, String message) {
        }

        @Override
        public void onMessage(Command command) {
        }

        @Override
        public void onError(String message) {
        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            mToast = Toast.makeText(LobbyActivity.this,
                    getText(R.string.bluetooth_connect_fail),
                    Toast.LENGTH_SHORT);
            mToast.show();
        }

        @Override
        public void onAccept(String playerName) {
        }
    }

    private class CommunicationCallbackClient implements CommunicationCallback {
        @Override
        public void onConnect(BluetoothDevice device) {
        }

        @Override
        public void onDisconnect(BluetoothDevice device, String message) {
        }

        @Override
        public void onMessage(Command command) {
        }

        @Override
        public void onError(String message) {
        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            mToast = Toast.makeText(LobbyActivity.this,
                    getText(R.string.bluetooth_connect_fail),
                    Toast.LENGTH_SHORT);
            mToast.show();
        }

        @Override
        public void onAccept(String playerName) {
            mPlayers.add(playerName);
        }
    }
}
