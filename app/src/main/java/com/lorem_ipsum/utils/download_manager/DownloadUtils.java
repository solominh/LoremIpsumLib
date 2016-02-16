package com.lorem_ipsum.utils.download_manager;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.lorem_ipsum.BuildConfig;
import com.lorem_ipsum.utils.AppUtils;
import com.lorem_ipsum.utils.ObjectUtils;
import com.lorem_ipsum.utils.StringUtils;

/**
 * Created by hoangminh on 9/10/15.
 */

/*
Put this receiver to AndroidManifest
 <!-- Download receiver -->
        <receiver
            android:name=".managers.download_manager.DownloadReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.DOWNLOAD_COMPLETE"/>
            </intent-filter>
        </receiver>
 */

    
public class DownloadUtils {

    private DownloadManager mDownloadManager;
    private Context mContext;

    private static DownloadUtils instance = null;

    private DownloadUtils() {
        mContext = AppUtils.getAppContext();
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static DownloadUtils getInstance() {
        if (instance == null) {
            instance = new DownloadUtils();
        }
        return instance;
    }

    public long download(String url, String fileDestination) {
        // Sanity check
        if(StringUtils.isNull(url) || StringUtils.isNull(fileDestination))
            return -1;

        // Create uri
        Uri uri = Uri.parse(url);

        // Create request options
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(url);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        // Get download dir
        String appName = AppUtils.getAppPackageName();
        String downloadDir = appName + "/Temp";
        String fileName = StringUtils.md5Hash(url);

        // Set download location
        if (BuildConfig.DEBUG) {
            request.setDestinationInExternalPublicDir(downloadDir, fileName);   // Data will not be delete when uninstall app
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE); // Show downloading ui
        } else {
            request.setDestinationInExternalFilesDir(mContext, downloadDir, fileName); //  Data will be delete when uninstall app
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN); // Hide downloading ui
            request.setVisibleInDownloadsUi(false); // Need permission in androidManifest
        }

        // Create DownloadFileInfo for request description
        DownloadFileInfo info = new DownloadFileInfo(appName, null, url, fileDestination);
        String jsonString = ObjectUtils.toJson(info);
        request.setDescription(jsonString);

        // Return download id for receiver
        return mDownloadManager.enqueue(request);
    }



}
