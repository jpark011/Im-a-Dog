package com.cs446w18.a16.imadog;

import android.content.Context;
import android.graphics.Typeface;

import com.cs446w18.a16.imadog.bluetooth.BluetoothServer;
import com.cs446w18.a16.imadog.controller.User;
import com.cs446w18.a16.imadog.controller.UserController;

import java.util.HashMap;

/**
 * Created by lacie on 2018-02-23.
 */

public class Global {
    public static UserController user = null;

    // Global array of fonts
    public static HashMap<String, Typeface> fonts;
    public static void initFonts(Context context)
    {
        HashMap<String,Typeface> fontsMap = new HashMap();

        fontsMap.put("LeagueSpartan", Typeface.createFromAsset(context.getAssets(), "fonts/LeagueSpartan.otf"));
        fontsMap.put("OSBold", Typeface.createFromAsset(context.getAssets(), "fonts/OSBold.ttf"));
        fontsMap.put("OSBoldItalic", Typeface.createFromAsset(context.getAssets(), "fonts/OSBoldItalic.ttf"));
        fontsMap.put("OSExtraBold", Typeface.createFromAsset(context.getAssets(), "fonts/OSExtraBold.ttf"));
        fontsMap.put("OSExtraBoldItalic", Typeface.createFromAsset(context.getAssets(), "fonts/OSExtraBoldItalic.ttf"));
        fontsMap.put("OSItalic", Typeface.createFromAsset(context.getAssets(), "fonts/OSItalic.ttf"));
        fontsMap.put("OSLight", Typeface.createFromAsset(context.getAssets(), "fonts/OSLight.ttf"));
        fontsMap.put("OSLightItalic", Typeface.createFromAsset(context.getAssets(), "fonts/OSLightItalic.ttf"));
        fontsMap.put("OSRegular", Typeface.createFromAsset(context.getAssets(), "fonts/OSRegular.ttf"));
        fontsMap.put("OSSemibold", Typeface.createFromAsset(context.getAssets(), "fonts/OSSemibold.ttf"));
        fontsMap.put("OSSemiboldItalic", Typeface.createFromAsset(context.getAssets(), "fonts/OSSemiboldItalic.ttf"));

        Global.fonts = fontsMap;
    }

}
