package com.cs446w18.a16.imadog.activities.menu;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.bluetooth.Bluetooth;
import com.cs446w18.a16.imadog.bluetooth.CommunicationCallback;
import com.cs446w18.a16.imadog.bluetooth.DiscoveryCallback;
import com.cs446w18.a16.imadog.commands.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class JoinGameActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    private RoomsListAdapter adapter;
    private List<BluetoothDevice> mRooms;

    private ListView roomsListView;

    private Toast mToast;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        // List view
        roomsListView = findViewById(R.id.roomsListView);

        // TODO: Replace with real rooms
        ArrayList<BluetoothDevice> testList = new ArrayList<>();
//        testList.add("Alice's Room");
//        testList.add("Room 45");
        mRooms = testList;
        adapter = new RoomsListAdapter(this);
        roomsListView.setAdapter(adapter);
        roomsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        roomsListView.setSelector(R.drawable.row_selector);


        // Set row selection action
        roomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Global.user.joinRoom(mRooms.get(i));
                Intent joinIntent = new Intent(JoinGameActivity.this, LobbyActivity.class);
                startActivity(joinIntent);
            }
        });

        Bluetooth client = Global.user.getClient();
        client.setDiscoveryCallback(new DiscoveryCallBackClient());
        List<BluetoothDevice> pairedDevices = client.getPairedDevices();
        mRooms.addAll(pairedDevices);
        Global.user.searchRoom(this);
    }


    /* ----------------------------- METHODS ----------------------------- */


    /* ----------------------------- LIST ADAPTER ----------------------------- */

    private class RoomsListAdapter extends BaseAdapter {

        private Context mContext;

        public RoomsListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mRooms.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) { return true; }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // Get the row
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View row = inflater.inflate(R.layout.row_join_game, viewGroup, false);

            // Change the name
            TextView nameLabel = row.findViewById(R.id.nameLabel);
            BluetoothDevice device = mRooms.get(i);
            nameLabel.setText(device.getName() + "\n" + device.getAddress());

            return row;
        }
    }

    private class DiscoveryCallBackClient implements DiscoveryCallback {
        @Override
        public void onFinish() {
        }

        @Override
        public void onDevice(BluetoothDevice device) {
            mRooms.add(device);
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onPair(BluetoothDevice device) {
            mToast = Toast.makeText(JoinGameActivity.this,
                    getText(R.string.bluetooth_paired),
                    Toast.LENGTH_SHORT);
            mToast.show();
        }

        @Override
        public void onUnpair(BluetoothDevice device) {
            mToast = Toast.makeText(JoinGameActivity.this,
                    getText(R.string.bluetooth_warning),
                    Toast.LENGTH_SHORT);
            mToast.show();
        }

        @Override
        public void onError(String message) {
            mToast = Toast.makeText(JoinGameActivity.this,
                    getText(R.string.bluetooth_warning),
                    Toast.LENGTH_SHORT);
            mToast.show();

        }

        @Override
        public void onDiscoverable() {
        }
    }
}

