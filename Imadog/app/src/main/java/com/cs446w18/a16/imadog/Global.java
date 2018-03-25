package com.cs446w18.a16.imadog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;

import com.cs446w18.a16.imadog.presenter.UserPresenter;

import java.util.HashMap;

public class Global {
    public static UserPresenter user = null;

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

    private static boolean countDownStarted = false;
    private static long currentTime;
    public static CountDownTimer timer;

    public static void startTimer(long t) {
        countDownStarted = true;
        currentTime = t;
    }

    public static void endTimer() {
        if (countDownStarted) {
            timer.cancel();
            countDownStopped();
        }
    }

    public static long getCurrentTime() {
        return currentTime;
    }

    public static void setCurrentTime(long t) {
        currentTime = t;
    }

    public static boolean isCountDownStarted() {
        return countDownStarted;
    }

    public static void countDownStopped() {
        countDownStarted = false;
    }

}
