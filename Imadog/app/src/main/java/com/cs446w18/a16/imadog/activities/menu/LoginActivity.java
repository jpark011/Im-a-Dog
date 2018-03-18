package com.cs446w18.a16.imadog.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;
import com.cs446w18.a16.imadog.controller.UserController;

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

                    String name = textView.getText().toString();
                    if (Global.user == null) {
                        Global.user = new UserController(name);
                    }


                    Intent menuIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(menuIntent);

                }
                return false;

            }
        };
        nameField.setOnEditorActionListener(fieldListener);

        // Setups the fonts
        Global.initFonts(getApplicationContext());

    }


    /* ----------------------------- METHODS ----------------------------- */


}
