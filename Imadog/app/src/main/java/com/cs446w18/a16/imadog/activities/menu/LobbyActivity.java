package com.cs446w18.a16.imadog.activities.menu;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
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
        Global.user.setLobby(this);

        // Players list
        VoteListView playersList = findViewById(R.id.playersListView);
        ArrayList<String> players;

        mPlayers = new ArrayList<>();
        if (Global.user.isServer()) {
            players = Global.user.getRoomMembers();
            Global.user.getServer().setCommunicationCallback(new CommunicationCallbackServer());
        } else {
            players = new ArrayList<>();
            players.add("Loading room members...");
            Global.user.getClient().setCommunicationCallback(new CommunicationCallbackClient());
            Button startButton = findViewById(R.id.startButton);
            startButton.setVisibility(View.GONE);
        }

        playersList.setupBlackTheme(players);
        playersList.isEnabled = false;
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Start button is pressed
    public void startGame(View view) {
        Global.user.startGame();
    }

    public void openGameActivity() {
        Intent startGameIntent = new Intent(LobbyActivity.this, GameActivity.class);
        startActivity(startGameIntent);
    }

    public void updateLobbyMembers(ArrayList<String> members) {
        final ArrayList<String> players = members;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                VoteListView playersList = findViewById(R.id.playersListView);
                playersList.setupBlackTheme(players);
                playersList.isEnabled = false;
            }
        });
    }

    public void leaveRoom(View view) {
        Global.user.leaveRoom();
    }

    public void toMainActivity() {
        Intent mainIntent = new Intent(LobbyActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mainIntent);
    }


    /* ----------------------------- BLUETOOTH ----------------------------- */

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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToast = Toast.makeText(LobbyActivity.this,
                            getText(R.string.bluetooth_connect_fail),
                            Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
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
        public void onMessage(Command cmd) {
            cmd.setReceiver(Global.user);
            cmd.execute();
        }

        @Override
        public void onError(String message) {
        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToast = Toast.makeText(LobbyActivity.this,
                            getText(R.string.bluetooth_connect_fail),
                            Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
            System.out.println("Connect ERROR: "+ message);
        }

        @Override
        public void onAccept(String playerName) {
            mPlayers.add(playerName);
        }
    }
}
