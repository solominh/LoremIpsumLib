package com.lorem_ipsum.utils;

import android.util.Log;

import com.lorem_ipsum.BuildConfig;
import com.lorem_ipsum.models.MyLog;
import com.lorem_ipsum.modules.event_bus.MyEventBus;

/**
 * Created by originally.us on 4/13/14.
 */
public final class LogUtils {

    public static void v(String logTag, String message) {
        if (BuildConfig.DEBUG)
            Log.v(logTag, message);
    }

    public static void d(String logTag, String message) {
        if (BuildConfig.DEBUG)
            LogUtils.e(logTag, message);
        MyEventBus.getCustom().post(new MyLog(logTag, message, MyLog.DEBUG));
    }

    public static void i(String logTag, String message) {
        if (BuildConfig.DEBUG)
            Log.i(logTag, message);
        MyEventBus.getCustom().post(new MyLog(logTag, message, MyLog.INFO));
    }

    public static void w(String logTag, String message) {
        if (BuildConfig.DEBUG)
            Log.w(logTag, message);
        MyEventBus.getCustom().post(new MyLog(logTag, message, MyLog.WARNING));
    }

    public static void e(String logTag, String message) {
        if (BuildConfig.DEBUG)
            LogUtils.e(logTag, message);
        MyEventBus.getCustom().post(new MyLog(logTag, message, MyLog.ERROR));
    }

    public static void e(String logTag, String message, Throwable e) {
        if (BuildConfig.DEBUG)
            LogUtils.e(logTag, message, e);
    }

}
