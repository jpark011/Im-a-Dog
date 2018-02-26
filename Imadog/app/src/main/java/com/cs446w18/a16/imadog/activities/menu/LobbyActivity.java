package com.cs446w18.a16.imadog.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.GameActivity;
import com.cs446w18.a16.imadog.activities.SuperActivity;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class LobbyActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Start button is pressed
    public void startGame(View view) {
        Global.user.startGame();
        Intent startGameIntent = new Intent(LobbyActivity.this, GameActivity.class);
        startActivity(startGameIntent);
    }


}
