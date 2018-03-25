package com.cs446w18.a16.imadog.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.activities.menu.CreateGameActivity;
import com.cs446w18.a16.imadog.activities.menu.LobbyActivity;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class SuperActivity extends AppCompatActivity {


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);

        // Fullscreen mode
        hideSystemUI();

        // Background
        getWindow().getDecorView().setBackgroundResource(R.drawable.background);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
    }


    /* ----------------------------- METHODS ----------------------------- */

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /// Changes the background color while keeping the borders
    protected void changeBackgroudColor(int color) {
        GradientDrawable background = (GradientDrawable)getWindow().getDecorView().getBackground().getConstantState().newDrawable().mutate();;
        background.setColor(ContextCompat.getColor(this, color));
        getWindow().getDecorView().setBackground(background);
    }


    /// CALLBACK
    public void back(View view) {
        hideSystemUI();
        finish();
    }


}
