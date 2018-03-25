package com.cs446w18.a16.imadog.activities.menu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    Toast mToast;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        // Page background
        changeBackgroudColor(R.color.light_green);

        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();

        // Name label
        TextView nameLabel = findViewById(R.id.nameLabel);
        nameLabel.setText(deviceName);

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
            Global.user.createGame("");

            Intent createIntent = new Intent(CreateGameActivity.this, LobbyActivity.class);
            startActivity(createIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        Global.user.getServer().onActivityResult(requestCode, resultCode, intent);
    }
}
