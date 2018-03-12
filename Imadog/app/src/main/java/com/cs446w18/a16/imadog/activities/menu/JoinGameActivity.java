package com.cs446w18.a16.imadog.activities.menu;

import android.content.Context;
import android.content.Intent;
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

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class JoinGameActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    private List<String> mRooms;

    private ListView roomsListView;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        // List view
        roomsListView = findViewById(R.id.roomsListView);

        // TODO: Replace with real rooms
        ArrayList<String> testList = new ArrayList<>();
        testList.add("Alice's Room");
        testList.add("Room 45");
        mRooms = testList;
        roomsListView.setAdapter(new RoomsListAdapter(this));
        roomsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        roomsListView.setSelector(R.drawable.row_selector);



        // Set row selection action
        roomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent joinIntent = new Intent(JoinGameActivity.this, LobbyActivity.class);
                startActivity(joinIntent);
            }
        });

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
            nameLabel.setText(mRooms.get(i));

            return row;
        }
    }


}

