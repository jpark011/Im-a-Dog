package com.cs446w18.a16.imadog.activities.menu;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.controller.User;

/**
 * Created by Jean-Baptiste on 25/02/2018.
 */

public class LoginActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    EditText nameField;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Answer field
        nameField = findViewById(R.id.nameField);
        nameField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        nameField.setTypeface(Global.fonts.get("OSSemibold"));

        GradientDrawable background = (GradientDrawable)nameField.getBackground().getConstantState().newDrawable().mutate();;
        background.setColor(ContextCompat.getColor(this, R.color.white));
        nameField.setBackground(background);

        // Set the return action
        TextView.OnEditorActionListener fieldListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent != null &&
                                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    // When the user press enter
                    hideSystemUI();

                }
                return false;

            }
        };
        nameField.setOnEditorActionListener(fieldListener);

    }


    /* ----------------------------- METHODS ----------------------------- */

    /// CALLBACK: when the Continue button is pressed
    public void continueWasPressed(View view) {
        hideSystemUI();

        String name = nameField.getText().toString();
        if (Global.user == null) {
            Global.user = new User(name);
        }

        Intent menuIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(menuIntent);
    }


}
