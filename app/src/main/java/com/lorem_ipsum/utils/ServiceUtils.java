package com.lorem_ipsum.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by hoangminh on 2/1/16.
 */
public class ServiceUtils {

    public static boolean isServiceRunning(Class<?> serviceClass) {
        Context context = AppUtils.getAppContext();

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
