package com.lorem_ipsum.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Torin on 21/12/15.
 */
public class ClockUtils {

    private static Long deltaSeconds = null;

    private static final String SHARED_PREFS_NAME = "ClockUtils_Shared_Prefs";
    private static final String SHARED_PREFS_KEY = "ClockUtils_Delta_Seconds";

    public static void updateDeltaWithServerTime(Context context, long serverSeconds) {
        if (serverSeconds <= 1000) {
            deltaSeconds = 0L;
            return;
        }

        // Static cache
        long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        deltaSeconds = serverSeconds - currentSeconds;

        // Save to SharedPrefs
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putLong(SHARED_PREFS_KEY, deltaSeconds);
        editor.apply();
    }

    public static long currentTimeMillis() {
        // Retrieve from SharedPrefs
        if (deltaSeconds == null) {
            SharedPreferences sharedPreferences = getSharedPreferences(null);
            if (sharedPreferences == null)
                return System.currentTimeMillis();

            boolean hasSavedValue = sharedPreferences.getLong(SHARED_PREFS_KEY, 0) == sharedPreferences.getLong(SHARED_PREFS_KEY, -1);
            if (!hasSavedValue)
                return System.currentTimeMillis();

            deltaSeconds = sharedPreferences.getLong(SHARED_PREFS_KEY, 0);
        }

        long currentMillis = System.currentTimeMillis();
        return currentMillis + (deltaSeconds * 1000);
    }

    public static long currentTimeSeconds() {
        long millis = currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }


    public static Date now() {
        return new Date(currentTimeMillis());
    }


    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private static SharedPreferences getSharedPreferences(Context context) {
        if (context == null)
            context = AppUtils.getAppContext();
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }
}
