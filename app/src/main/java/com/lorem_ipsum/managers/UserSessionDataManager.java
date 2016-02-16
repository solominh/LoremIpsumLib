package com.lorem_ipsum.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.lorem_ipsum.models.User;
import com.lorem_ipsum.utils.AppUtils;
import com.lorem_ipsum.utils.GsonUtils;
import com.lorem_ipsum.utils.LogUtils;
import com.lorem_ipsum.utils.StringUtils;

/**
 * Created by originally.us on 5/10/14.
 */
public class UserSessionDataManager {

    private static final String LOG_TAG = "UserSessionDataManager";

    private static final String PREF_KEY_ACCESS_TOKEN_DATA = "access_token_data";
    private static final String PREF_KEY_USER_INFO = "key_user_info";
    private static final String PREF_KEY_LICENSE_KEY = "key_license_key";

    // memory cache
    private static User cacheUser;
    private static String cacheAccessToken;
    private static String licenseKey;

    /*
     * Get share preferences
     */
    private static SharedPreferences getSharedPreferences() {
        Context ctx = AppUtils.getAppContext();
        return ctx.getSharedPreferences("pref_user_session_data", Context.MODE_PRIVATE);
    }

    /**
     * Helper method to clear all user-related cached data before logging out
     */
    public static void clearAllSavedUserData() {
        cacheUser = null;
        cacheAccessToken = null;
        getSharedPreferences().edit().clear().apply();
    }

    //*************************************************************************
    // Authorization token (access token for all authenticated APIs)
    //*************************************************************************

    /**
     * Get authorization token (not facebook token)
     */
    public static String getCurrentAccessToken() {
        if (StringUtils.isNotNull(cacheAccessToken))
            return cacheAccessToken;

        String accessToken = null;
        User user = getCurrentUser();
        if (user != null)
            accessToken = user.secret;
        if (accessToken != null)
            cacheAccessToken = accessToken;

        return cacheAccessToken;
    }

    //*************************************************************************
    // GCM
    //*************************************************************************

    /**
     * Helper method to cache GCM token
     *
     * @param gcmDeviceId GCM Device ID (or token)
     */
    public static void saveGCMToken(String gcmDeviceId) {
        String appVersion = AppUtils.getAppVersionName();
        String cacheKey = "gcm_reg_id_" + appVersion;

        getSharedPreferences().edit().putString(cacheKey, gcmDeviceId).apply();
        Log.i(LOG_TAG, "Caching GCM token for app version " + appVersion);
    }

    /**
     * Helper method to get cached GCM token
     */
    public static String getGCMToken() {
        String appVersion = AppUtils.getAppVersionName();
        String cacheKey = "gcm_reg_id_" + appVersion;
        return getSharedPreferences().getString(cacheKey, null);
    }

    //*************************************************************************
    // User info
    //*************************************************************************

    public static void saveCurrentUser(User user) {
        // Sanity check
        if (user == null)
            return;

        // Memory cache
        cacheUser = user;
        cacheAccessToken = user.secret;

        // Convert user object to string
        Gson gson = GsonUtils.getGson();
        String json = gson.toJson(user);
        if (StringUtils.isNull(json))
            return;

        // Save
        getSharedPreferences().edit().putString(PREF_KEY_USER_INFO, json).apply();
    }

    public static void saveLicenseKey(String string) {
        // Sanity check
        if (string == null)
            return;

        // Memory cache
        licenseKey = string;

        // Save
        getSharedPreferences().edit().putString(PREF_KEY_LICENSE_KEY, string).apply();
    }

    public static User getCurrentUser() {
        if (cacheUser != null)
            return cacheUser;

        // Get user string
        String jsonData = getSharedPreferences().getString(PREF_KEY_USER_INFO, "");

        // Convert back to User data model
        User user = null;
        Gson gson = GsonUtils.getGson();
        try {
            user = gson.fromJson(jsonData, User.class);
        } catch (Exception e) {
            String message = e.getMessage();
            LogUtils.d(LOG_TAG, "getCurrentUserInfo error: " + message);
        }

        // Memory cache
        cacheUser = user;
        if (user != null)
            cacheAccessToken = user.secret;

        return cacheUser;
    }

    public static String getLicenseKey() {
        if (licenseKey != null)
            return licenseKey;

        licenseKey = getSharedPreferences().getString(PREF_KEY_LICENSE_KEY, "");

        return licenseKey;
    }


    public static Number getCurrentUserID() {
        User currentUser = getCurrentUser();
        return currentUser == null ? null : currentUser.id;
    }

    //*************************************************************************
    // App preference
    //*************************************************************************

    public static void saveLanguagePref(int langId) {
        if (langId <= 0)
            langId = 1;

        getSharedPreferences().edit().putInt("langId", langId).apply();
    }

    public static int getLanguagePref() {
        return getSharedPreferences().getInt("langId", 1);
    }
}
