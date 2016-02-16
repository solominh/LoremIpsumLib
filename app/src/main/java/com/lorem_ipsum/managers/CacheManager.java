package com.lorem_ipsum.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lorem_ipsum.utils.AppUtils;
import com.lorem_ipsum.utils.GsonUtils;
import com.lorem_ipsum.utils.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Originally.US on 27/6/14.
 */
public class CacheManager {

    private static final String SHARED_PREF_CACHE_DATA = "SHARED_PREF_CACHE_DATA";
    private static final String KEY_CREATED_TIMESTAMP = "_created_at";

    public static SharedPreferences getSharedPreferences() {
        // Make sure each logged in user has their own cache sandbox
        Number currentUserId = UserSessionDataManager.getCurrentUserID();
        if (currentUserId == null)
            currentUserId = 0;

        Context context = AppUtils.getAppContext();
        if (context == null)
            return null;

        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_CACHE_DATA + "_" + currentUserId, 0);
        return settings;
    }

   //-----------------------------------------------------------------------------
   // List - hoangminh - 5:42 PM - 2/1/16
   //-----------------------------------------------------------------------------

    public static void saveListCacheData(final String key, ArrayList list) {
        saveObjectCacheData(key, list);
    }

    public static void removeListCacheData(final String key) {
        removeObjectCacheData(key);
    }

    public static <T> ArrayList<T> getListCacheData(final String key, Type type, int expiry_minutes) {
        // Sanity check
        if (key == null)
            return null;

        // No expiry date
        if (expiry_minutes <= 0)
            return getListCacheData(key, type);

        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return null;

        // When was it created
        long createdTimestamp = getSharedPreferences().getLong(key + KEY_CREATED_TIMESTAMP, 0);
        if (createdTimestamp <= 0)
            return null;

        // Not expired yet
        long secondDelta = (System.currentTimeMillis() - createdTimestamp) / 1000;
        if (secondDelta < expiry_minutes * 60)
            return getListCacheData(key, type);

        // Cache miss
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> getListCacheData(final String key, Type type) {
        // Sanity check
        if (key == null)
            return null;
        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return null;

        // Get jsonString
        String jsonString = settings.getString(key, null);
        if (jsonString == null || jsonString.length() <= 0)
            return null;

        // Get object
        Gson gson = GsonUtils.getGson();
        Object object = null;
        try {
            object = gson.fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cast object
        return  (ArrayList<T>)object;
    }

    //-----------------------------------------------------------------------------
    // Cache String - hoangminh - 5:38 PM - 2/1/16
    //-----------------------------------------------------------------------------

    public static void saveStringCacheData(final String key, String data) {
        if (key == null || data == null)
            return;

        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return;
        SharedPreferences.Editor editor = settings.edit();
        if (editor == null)
            return;

        editor.putString(key, data);
        editor.apply();
    }

    public static String getStringCacheData(String key) {
        if (key == null)
            return null;

        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return null;

        return settings.getString(key, null);
    }

    //-----------------------------------------------------------------------------
    // Cache object - hoangminh - 5:37 PM - 2/1/16
    //-----------------------------------------------------------------------------

    public static void saveObjectCacheData(final String key, Object object) {
        // Sanity check
        if (key == null || object == null)
            return;
        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return;
        SharedPreferences.Editor editor = settings.edit();
        if (editor == null)
            return;

        // Serialize object
        Gson gson = GsonUtils.getGson();
        String jsonString = gson.toJson(object);

        // Save it & the timestamp it was save
        if (StringUtils.isNotNull(jsonString)) {
            editor.putString(key, jsonString);
            editor.putLong(key + KEY_CREATED_TIMESTAMP, System.currentTimeMillis());
        }

        editor.apply();
    }

    public static void removeObjectCacheData(final String key) {
        // Sanity check
        if (key == null)
            return;
        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return;
        SharedPreferences.Editor editor = settings.edit();
        if (editor == null)
            return;

        editor.remove(key);
        editor.remove(key + KEY_CREATED_TIMESTAMP);
        editor.apply();
    }

    public static <T> T getObjectCacheData(final String key, Class<T> type) {
        // Sanity check
        if (key == null)
            return null;
        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return null;

        // Get jsonString
        String jsonString = settings.getString(key, null);
        if (jsonString == null || jsonString.length() <= 0)
            return null;

        // Convert json to object
        Gson gson = GsonUtils.getGson();
        Object object = null;
        try {
            object = gson.fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return type.cast(object);
    }

    public static <T> T getObjectCacheData(final String key, Class<T> type, int expiry_minutes) {
        // Sanity check
        if (key == null)
            return null;

        // No expiry time
        if (expiry_minutes <= 0)
            return getObjectCacheData(key, type);

        SharedPreferences settings = getSharedPreferences();
        if (settings == null)
            return null;

        // When was it created
        long createdTimestamp = getSharedPreferences().getLong(key + KEY_CREATED_TIMESTAMP, 0);
        if (createdTimestamp <= 0)
            return null;

        // Not expired yet
        long secondDelta = (System.currentTimeMillis() - createdTimestamp) / 1000;
        if (secondDelta < expiry_minutes * 60)
            return getObjectCacheData(key, type);

        //Cache miss
        return null;
    }

}
