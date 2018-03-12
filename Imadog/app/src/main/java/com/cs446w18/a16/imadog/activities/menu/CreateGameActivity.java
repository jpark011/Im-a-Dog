package com.cs446w18.a16.imadog.activities.menu;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class CreateGameActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    // Name field
    EditText nameField;


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

    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Create button is pressed
    public void createGame(View view) {
        hideSystemUI();
        String roomName = nameField.getText().toString();
        Global.user.createGame(getApplicationContext(), roomName);
        Intent createIntent = new Intent(CreateGameActivity.this, LobbyActivity.class);
        startActivity(createIntent);
    }


}
