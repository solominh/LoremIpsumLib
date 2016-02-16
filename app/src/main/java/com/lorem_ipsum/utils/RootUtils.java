package com.lorem_ipsum.utils;

import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class RootUtils {
    private static String TAG = "RootUtils";

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    private static boolean checkRootMethod4() {
        return isPackageInstalled("eu.chainfire.supersu");
    }

    private static boolean isPackageInstalled(String packagename) {
        PackageManager pm = AppUtils.getAppContext().getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean executeShellCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {
                }
            }
        }
    }

    public static boolean changeSystemTime(long seconds) {
        if (seconds <= 0)
            return false;

        boolean success = false;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.kkmmss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(seconds));
            String dateString = formatter.format(calendar.getTime());
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command = "date -s " + dateString + "\n";
            os.writeBytes(command);
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            success = true;
            LogUtils.e(TAG, "Change system time successful");
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            LogUtils.e(TAG, "Change system time failed");
        }
        return success;
    }

    public static void AutoInstallAPK(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            Process p = null;
            try {
                p = Runtime.getRuntime().exec("su");
                DataOutputStream outs = new DataOutputStream(p.getOutputStream());

                String cmd = "pm install " + file.getAbsolutePath();
                outs.writeBytes(cmd + "\n");
                LogUtils.e(TAG, "APK file installed: " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogUtils.e(TAG, "APK file not existed");
        }
    }
}