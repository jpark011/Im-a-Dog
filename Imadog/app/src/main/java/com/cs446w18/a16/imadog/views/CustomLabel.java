package com.cs446w18.a16.imadog.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 2018-03-08.
 */

public class CustomLabel extends AppCompatTextView {

    /* ----------------------------- ATTRIBUTES ----------------------------- */



    /* ----------------------------- SETUP ----------------------------- */

    public CustomLabel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomLabel);
        String customFont = a.getString(R.styleable.CustomLabel_customFont);
        setTypeface(Global.fonts.get(customFont));
        a.recycle();
    }


    /* ----------------------------- METHODS ----------------------------- */



}
