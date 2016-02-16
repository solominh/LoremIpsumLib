package com.lorem_ipsum.modules.device_policy;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by viethoa on 9/3/15.
 */
public class DeviceAdmin {

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mDevicePolicyAdmin;
    private Context mContext;

    public DeviceAdmin(Context context) {
        this.mContext = context;
        init();
    }

    public boolean isMyDevicePolicyReceiverActive() {
        return mDevicePolicyManager.isAdminActive(mDevicePolicyAdmin);
    }

    public void init() {
        mDevicePolicyManager = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDevicePolicyAdmin = new ComponentName(mContext, DevicePolicyReceiver.class);
    }

    public void allowUninstall() {
        mDevicePolicyManager.removeActiveAdmin(mDevicePolicyAdmin);
    }

    public void notAllowUninstall() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDevicePolicyAdmin);
        mContext.startActivity(intent);
    }
}
