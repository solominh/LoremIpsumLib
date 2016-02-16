package com.lorem_ipsum.utils.download_manager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.lorem_ipsum.utils.LogUtils;
import com.lorem_ipsum.utils.ObjectUtils;
import com.lorem_ipsum.utils.ThreadUtils;

import java.io.File;

/**
 * Created by hoangminh on 9/11/15.
 */
public class DownloadReceiver extends BroadcastReceiver {

    public final static String TAG = "Download Receiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {
                processDownloadFile(context, intent);
            }
        });
    }

    private static void processDownloadFile(Context context, Intent intent) {
        // Check download complete
        String action = intent.getAction();
        if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action))
            return;

        // Get Cursor
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor == null)
            return;

        cursor.moveToFirst();

        // Get description
        int descriptionIndex = cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION);
        String description = cursor.getString(descriptionIndex);

        // Get download file info from description
        DownloadFileInfo info = ObjectUtils.fromJson(description, DownloadFileInfo.class);
        if (info == null)
            return;

        // Get Status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);

        // Get file path
        int fileNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String savedFilePath = cursor.getString(fileNameIndex);
        if (savedFilePath == null) {
            cursor.close();
            return;
        }

        // The file we supply for download is TempFile
        File tempFile = new File(savedFilePath);
        if (!tempFile.exists())
            return;

        // Get Uri
        int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
        String uri = cursor.getString(uriIndex);

        // Get reason
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);

        switch (status) {
            case DownloadManager.STATUS_SUCCESSFUL:
                LogUtils.d(TAG, "SUCCESSFUL: " + savedFilePath);

                // Rename temp file to destination file
                File file = new File(info.filePath);
                if (!file.exists()) {
                    tempFile.renameTo(file);
                }

                break;
            case DownloadManager.STATUS_FAILED:
                LogUtils.d(TAG, "FAILED: " + reason);
                break;
            case DownloadManager.STATUS_PAUSED:
                LogUtils.d(TAG, "PAUSED: " + reason);
                break;
            case DownloadManager.STATUS_PENDING:
                LogUtils.d(TAG, "PENDING: " + reason);
                break;
            case DownloadManager.STATUS_RUNNING:
                LogUtils.d(TAG, "RUNNING: " + reason);
                break;
        }

        // Close cursor
        cursor.close();
    }
}
