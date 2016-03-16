package com.lorem_ipsum.managers.share_pref;

import android.content.Context;

import com.lorem_ipsum.utils.AppUtils;
import com.lorem_ipsum.utils.DeviceUtils;
import com.lorem_ipsum.utils.StringUtils;

/**
 * Created by hoangminh on 3/16/16.
 */
public class DemoSetting extends BaseSetting {

    private final static String TAG = DemoSetting.class.getSimpleName();
    protected String mSettingKeyName;
    protected String mAndroidId;

    private static DemoSetting instance = null;

    private DemoSetting(){
    }

    public synchronized static DemoSetting getInstance(){
        if(instance == null){
            instance=new DemoSetting();
        }
        return instance;
    }

    //-----------------------------------------------------------------------------
    // Abstract - hoangminh - 12:34 PM - 3/16/16
    //-----------------------------------------------------------------------------

    @Override
    protected String getSettingMainKeyName() {
        if (mSettingKeyName != null)
            return mSettingKeyName;

        // Get android ID
        if (mAndroidId == null) {
            final Context context = AppUtils.getAppContext();
            mAndroidId = DeviceUtils.getDeviceUUID(context);
        }

        // Build key
        final String key = TAG;
        mSettingKeyName = new StringBuilder(key + mAndroidId + key + mAndroidId + key + mAndroidId).reverse().toString();

        // Hash key
        final String hashedKey = StringUtils.md5Hash(mSettingKeyName);
        if (hashedKey != null)
            mSettingKeyName = hashedKey;

        return mSettingKeyName;
    }

    @Override
    protected String getSettingNormalKey(final String key) {
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

}
