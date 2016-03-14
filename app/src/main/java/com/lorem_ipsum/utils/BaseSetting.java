package com.lorem_ipsum.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hoangminh on 1/5/16.
 */
public abstract class BaseSetting {

    protected String mSettingKeyName;
    protected String mAndroidId;

    //-----------------------------------------------------------------------------
    // Hash key - hoangminh - 5:22 PM - 2/1/16
    //-----------------------------------------------------------------------------

    protected abstract String getSettingKeyName();

    protected SharedPreferences getSharedPreferences() {
        Context context = AppUtils.getAppContext();
        if (context == null)
            return null;

        return context.getSharedPreferences(getSettingMainKeyName(), Context.MODE_PRIVATE);
    }

    public String getSettingMainKeyName() {
        if (mSettingKeyName != null)
            return mSettingKeyName;

        // Get android ID
        if (mAndroidId == null) {
            final Context context = AppUtils.getAppContext();
            mAndroidId = DeviceUtils.getDeviceUUID(context);
        }

        // Build key
        final String key = getSettingKeyName();
        mSettingKeyName = new StringBuilder(key + mAndroidId + key + mAndroidId + key + mAndroidId).reverse().toString();

        // Hash key
        final String hashedKey = StringUtils.md5Hash(mSettingKeyName);
        if (hashedKey != null)
            mSettingKeyName = hashedKey;

        return mSettingKeyName;
    }

    public String getSettingNormalKey(final String key) {
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

    public void setInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putInt(getSettingNormalKey(key), value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getInt(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache long
    //------------------------------------------------------------------------------------------------------------------------

    public void setLong(String key, long value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putLong(getSettingNormalKey(key), value);
        editor.apply();
    }

    public long getLong(String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getLong(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache String
    //------------------------------------------------------------------------------------------------------------------------

    public void setString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putString(getSettingNormalKey(key), value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getString(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache Float
    //------------------------------------------------------------------------------------------------------------------------

    public void setFloat(String key, float value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putFloat(getSettingNormalKey(key), value);
        editor.apply();
    }

    public float getFloat(String key, float defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getFloat(getSettingNormalKey(key), defaultValue);
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Cache Boolean
    //------------------------------------------------------------------------------------------------------------------------

    public void setBool(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor == null)
            return;

        editor.putBoolean(getSettingNormalKey(key), value);
        editor.apply();
    }

    public boolean getBool(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences == null)
            return defaultValue;

        return sharedPreferences.getBoolean(getSettingNormalKey(key), defaultValue);
    }

}
