package com.lorem_ipsum.modules.device_policy;

import android.annotation.TargetApi;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.lorem_ipsum.utils.LogUtils;

// Add this code to android manifest
/*
 <!-- Prevent device uninstall this app -->
        <receiver
            android:name="com.lorem_ipsum.modules.device_policy.DevicePolicyReceiver"
            android:description="@string/device_admin_description"
            android:label="@string/device_admin"
            android:permission="android.permission.BIND_DEVICE_ADMIN">
            <meta-data
                android:name="android.app.device_admin"
                android:resource="@xml/device_polices"/>

            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED"/>
                <action android:name="android.app.action.DEVICE_ADMIN_DISABLE_REQUESTED"/>
                <action android:name="android.app.action.DEVICE_ADMIN_DISABLED"/>
                <action android:name="android.app.action.ACTION_PASSWORD_CHANGED"/>
                <action android:name="android.app.action.ACTION_PASSWORD_EXPIRING"/>
                <action android:name="android.app.action.ACTION_PASSWORD_FAILED"/>
                <action android:name="android.app.action.ACTION_PASSWORD_SUCCEEDED"/>
            </intent-filter>
        </receiver>
 */

public class DevicePolicyReceiver extends DeviceAdminReceiver {

    private static final String LOG_TAG = "DevicePolicyReceiver";

    @Override
    public void onDisabled(Context context, Intent intent) {
        LogUtils.d(LOG_TAG, "Device Admin Disabled");
        Toast.makeText(context, "Device Admin Disabled",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        LogUtils.d(LOG_TAG, "Device Admin is now enabled");
        Toast.makeText(context, "Device Admin is now enabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        CharSequence disableRequestedSeq = "Requesting to disable Device Admin";
        return disableRequestedSeq;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        LogUtils.d(LOG_TAG, "Device password is now changed");
        Toast.makeText(context, "Device password is now changed",Toast.LENGTH_SHORT).show();

        DevicePolicyManager localDPM = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName localComponent = new ComponentName(context,
                DevicePolicyReceiver.class);
        localDPM.setPasswordExpirationTimeout(localComponent, 0L);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPasswordExpiring(Context context, Intent intent) {
        // This would require API 11 an above
        LogUtils.d(LOG_TAG, "Device password is going to expire, please change to a new password");
        Toast.makeText(context, "Device password is going to expire, please change to a new password",Toast.LENGTH_SHORT).show();

        DevicePolicyManager localDPM = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName localComponent = new ComponentName(context,
                DevicePolicyReceiver.class);
        long expr = localDPM.getPasswordExpiration(localComponent);
        long delta = expr - System.currentTimeMillis();
        boolean expired = delta < 0L;
        if (expired) {
            localDPM.setPasswordExpirationTimeout(localComponent, 10000L);
            Intent passwordChangeIntent = new Intent(
                    DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
            passwordChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(passwordChangeIntent);
        }
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        LogUtils.d(LOG_TAG, "Password failed");
        Toast.makeText(context, "Password failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        LogUtils.d(LOG_TAG, "Access Granted");
        Toast.makeText(context, "Access Granted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "DevicePolicyReciever Received: " + intent.getAction());
        super.onReceive(context, intent);
    }
}
