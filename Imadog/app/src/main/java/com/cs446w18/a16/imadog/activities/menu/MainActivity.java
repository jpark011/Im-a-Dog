package com.cs446w18.a16.imadog.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.bluetooth.Bluetooth;
import com.cs446w18.a16.imadog.bluetooth.BluetoothCallback;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.User;
import com.cs446w18.a16.imadog.views.CustomButton;

public class MainActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */
    Toast mBluetoothToast;

    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomButton joinButton = (CustomButton) findViewById(R.id.joinButton);
        joinButton.updateBackgroundColor(this, R.color.yellow);

        CustomButton createButton = (CustomButton) findViewById(R.id.createButton);
        createButton.updateBackgroundColor(this, R.color.green);

        CustomButton settingsButton = (CustomButton) findViewById(R.id.settingsButton);
        settingsButton.updateBackgroundColor(this, R.color.red);

        CharSequence text = getText(R.string.bluetooth_warning);
        mBluetoothToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Join button is pressed
    public void joinGame(View view) {
        Bluetooth client = new Bluetooth(this);
        client.setBluetoothCallback(new BluetoothCallbackClient());
        Global.user.setClient(client);
        if (!client.isEnabled()) {
            client.enable(this);
        } else {
            Intent joinGameIntent = new Intent(MainActivity.this, JoinGameActivity.class);
            startActivity(joinGameIntent);
        }
    }

    /// CALLBACK: when the Create button is pressed
    public void createGame(View view) {
        BluetoothServer server = new BluetoothServer(this, Global.user);
        server.setBluetoothCallback(new BluetoothCallbackClient());
        Global.user.setServer(server);
        if (!server.isEnabled()) {
            server.enable(this);
        } else {
            Intent createGameIntent = new Intent(MainActivity.this, CreateGameActivity.class);
            startActivity(createGameIntent);
        }

    }

    /// CALLBACK: when the Settings button is pressed
    public void showSettings(View view) {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    /* ----------------------------- LISTENER ----------------------------- */
    private class BluetoothCallbackClient implements BluetoothCallback {
        @Override
        public void onBluetoothTurningOn() {
            onBluetoothOn();
        }

        @Override
        public void onBluetoothOn() {
            if (Global.user.isServer()) {
                Intent createGameIntent = new Intent(MainActivity.this, CreateGameActivity.class);
                startActivity(createGameIntent);
            } else {
                Intent joinGameIntent = new Intent(MainActivity.this, JoinGameActivity.class);
                startActivity(joinGameIntent);
            }
        }

        @Override
        public void onBluetoothTurningOff() {
            mBluetoothToast.show();
        }

        @Override
        public void onBluetoothOff() {
            mBluetoothToast.show();
        }

        @Override
        public void onUserDeniedActivation() {
            mBluetoothToast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        if (Global.user.isServer()) {
            Global.user.getServer().onActivityResult(requestCode, resultCode, intent);
        } else {
            Global.user.getClient().onActivityResult(requestCode, resultCode, intent);
        }
    }
}
