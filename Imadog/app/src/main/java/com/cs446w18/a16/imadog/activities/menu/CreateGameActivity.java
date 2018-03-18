package com.cs446w18.a16.imadog.activities.menu;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.bluetooth.BluetoothCallback;
import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.bluetooth.DiscoveryCallback;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class CreateGameActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    // Name field
    EditText nameField;
    Toast mToast;

    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        nameField = findViewById(R.id.nameField);
        nameField.setTypeface(Global.fonts.get("OSSemibold"));

        GradientDrawable background = (GradientDrawable)nameField.getBackground().getConstantState().newDrawable().mutate();;
        background.setColor(ContextCompat.getColor(this, R.color.white));
        nameField.setBackground(background);

        Global.user.getServer().setDiscoveryCallback(new DiscoveryCallbackServer());
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Create button is pressed
    public void createGame(View view) {
        hideSystemUI();
        Global.user.openRoom(this);
    }

    /* ----------------------------- LISTENER ----------------------------- */
    class DiscoveryCallbackServer implements DiscoveryCallback {
        @Override
        public void onFinish() {
        }

        @Override
        public void onDevice(BluetoothDevice device) {
        }

        @Override
        public void onPair(BluetoothDevice device) {
        }

        @Override
        public void onUnpair(BluetoothDevice device) {
        }

        @Override
        public void onError(String message) {
            mToast = Toast.makeText(CreateGameActivity.this,
                    getText(R.string.bluetooth_warning),
                    Toast.LENGTH_SHORT);
            mToast.show();
        }

        @Override
        public void onDiscoverable() {
            String roomName = nameField.getText().toString();
            Global.user.createGame(roomName);

            Intent createIntent = new Intent(CreateGameActivity.this, LobbyActivity.class);
            startActivity(createIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        Global.user.getServer().onActivityResult(requestCode, resultCode, intent);
    }
}
