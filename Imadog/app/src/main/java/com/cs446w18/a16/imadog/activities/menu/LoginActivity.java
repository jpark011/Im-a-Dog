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
import com.cs446w18.a16.imadog.controller.UserController;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Jean-Baptiste on 25/02/2018.
 */

public class LoginActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    EditText nameField;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private CallbackManager callbackManager;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

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

        // Setups the fonts
        Global.initFonts(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(PUBLIC_PROFILE, EMAIL));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                String name = object.optString("name");

                                if (Global.user == null) {
                                    Global.user = new UserController(name);
                                }
                                Intent menuIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(menuIntent);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("Error");
            }
        });
    }


    /* ----------------------------- METHODS ----------------------------- */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /// CALLBACK: when the Continue button is pressed
    public void continueWasPressed(View view) {
        hideSystemUI();

        String name = nameField.getText().toString();
        if (Global.user == null) {
            Global.user = new UserController(name);
        }


        Intent menuIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(menuIntent);
    }






}
