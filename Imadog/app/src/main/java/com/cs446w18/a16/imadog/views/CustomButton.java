package com.cs446w18.a16.imadog.views;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Jean-Baptiste on 2018-03-09.
 */

public class CustomButton extends AppCompatButton {

    /* ----------------------------- ATTRIBUTES ----------------------------- */



    /* ----------------------------- SETUP ----------------------------- */

    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Custom font
        Typeface font = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/LeagueSpartan.otf");
        setTypeface(font);

        System.out.println("Button setup");
    }



    /* ----------------------------- METHODS ----------------------------- */

    public void updateBackgroundColor(Context context, int color) {
        GradientDrawable background = (GradientDrawable)getBackground().getConstantState().newDrawable().mutate();;
        background.setColor(ContextCompat.getColor(context, color));
        setBackground(background);
    }


}
