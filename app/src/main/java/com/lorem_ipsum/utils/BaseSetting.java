package com.lorem_ipsum.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hoangminh on 1/5/16.
 */
public class BaseSetting {

    protected static final String SETTINGS_KEY_NAME = "SETTINGS_KEY_NAME";
    protected static String mSettingKeyName;
    protected static String mAndroidId;

    protected static SharedPreferences getSharedPreferences() {
        Context context = AppUtils.getAppContext();
        if (context == null)
            return null;

        return context.getSharedPreferences(getSettingMainKeyName(), Context.MODE_PRIVATE);
    }

    //-----------------------------------------------------------------------------
    // Hash key - hoangminh - 5:22 PM - 2/1/16
    //-----------------------------------------------------------------------------

    public static String getSettingMainKeyName() {
        if (mSettingKeyName != null)
            return mSettingKeyName;

        // Get android ID
        if (mAndroidId == null) {
            final Context context = AppUtils.getAppContext();
            mAndroidId = DeviceUtils.getDeviceUUID(context);
        }

        // Build key
        final String key = SETTINGS_KEY_NAME;
        mSettingKeyName = new StringBuilder(key + mAndroidId + key + mAndroidId + key + mAndroidId).reverse().toString();

        // Hash key
        final String hashedKey = StringUtils.md5Hash(mSettingKeyName);
        if (hashedKey != null)
            mSettingKeyName = hashedKey;

        return mSettingKeyName;
    }

    public static String getSettingNormalKey(final String key) {
        // Get android ID
        if (mAndroidId == null) {
            final Context context = AppUtils.getAppContext();
            mAndroidId = DeviceUtils.getDeviceUUID(context);
        }

        // Build key
        final String tempKey = new StringBuilder(key + mAndroidId).reverse().toString();

        // Hash key
        String hashedKey = StringUtils.md5Hash(tempKey);
        if (hashedKey == null)
            hashedKey = tempKey;

        return hashedKey;
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache Integer
    //------------------------------------------------------------------------------------------------------------------------

    public static void setInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putInt(getSettingNormalKey(key), value);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getInt(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache long
    //------------------------------------------------------------------------------------------------------------------------

    public static void setLong(String key, long value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putLong(getSettingNormalKey(key), value);
        editor.apply();
    }

    public static long getLong(String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getLong(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache String
    //------------------------------------------------------------------------------------------------------------------------

    public static void setString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putString(getSettingNormalKey(key), value);
        editor.apply();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getString(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache Float
    //------------------------------------------------------------------------------------------------------------------------

    public static void setFloat(String key, float value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putFloat(getSettingNormalKey(key), value);
        editor.apply();
    }

    public static float getFloat(String key, float defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getFloat(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache Boolean
    //------------------------------------------------------------------------------------------------------------------------

    public static void setBool(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putBoolean(getSettingNormalKey(key), value);
        editor.apply();
    }

    public static boolean getBool(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getBoolean(getSettingNormalKey(key), defaultValue);
    }

}
