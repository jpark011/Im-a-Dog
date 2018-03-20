package com.cs446w18.a16.imadog;

import android.app.Application;

/**
 * Created by Jean-Baptiste on 2018-03-13.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        // Setups the fonts
        Global.initFonts(getApplicationContext());

    }
}
