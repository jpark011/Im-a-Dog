package com.cs446w18.a16.imadog.activities.menu;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.SuperActivity;

/**
 * Created by Jean-Baptiste on 17/02/2018.
 */

public class SettingsActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    EditText nameField;


    /* ----------------------------- METHODS ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Page background
        changeBackgroudColor(R.color.light_red);

        // Name field
        nameField = findViewById(R.id.nameField);
        nameField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        nameField.setTypeface(Global.fonts.get("OSSemibold"));
        nameField.setText(Global.user.getUserName());

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
                    Global.user.setUserName(textView.getText().toString());

                }
                return false;

            }
        };
        nameField.setOnEditorActionListener(fieldListener);


    }

}
