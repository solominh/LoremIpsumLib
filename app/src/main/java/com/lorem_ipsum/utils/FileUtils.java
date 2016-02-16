package com.lorem_ipsum.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DangTai on [15 March 2014].
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    //-----------------------------------------------------------------------------
    // Directory utils - hoangminh - 9:17 AM - 2/3/16
    //-----------------------------------------------------------------------------

    // Auto create missing dir
    public static boolean getDir(File dir) {
        if (dir.exists() || dir.mkdirs()) {
            LogUtils.d(TAG, "Get Directory " + dir.getName() + " successfully");
            return true;
        }
        LogUtils.e(TAG, "Directory " + dir.getName() + " failed to create");
        return false;
    }

    //-----------------------------------------------------------------------------
    //- App dir - hoangminh - 9:21 AM - 2/3/16

    // Only get app dir. Not guarantee the dir is available
    public static File getAppFile() {
        String appName = AppUtils.getAppPackageName();
        return new File(getPublicDir(), appName);
    }

    // Get app dir. Create missing dir
    public static File getAppDir() {
        File file = getAppFile();
        return getDir(file) ? file : null;
    }

    //-----------------------------------------------------------------------------
    //- Public dir - hoangminh - 9:42 AM - 2/3/16

    public static File getPublicDir() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getPublicPictureDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static File getPublicDownloadDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    //-----------------------------------------------------------------------------
    //- Public image dir - hoangminh - 9:22 AM - 2/3/16

    public static File getDefaultPublicImageDir() {
        String appName = AppUtils.getAppPackageName();
        return getPublicImageDir(appName);
    }

    public static File getPublicImageDir(String dirName) {
        File mediaStorageDir = new File(getPublicPictureDir(), dirName);
        return getDir(mediaStorageDir) ? mediaStorageDir : null;
    }

    //-----------------------------------------------------------------------------
    //- App image dir - hoangminh - 9:41 AM - 2/3/16

    public static File getDefaultAppImageDir() {
        String defaultImageDir = "Images";
        return getAppImageDir(defaultImageDir);
    }

    public static File getAppImageDir(String dirName) {
        File file = new File(getAppFile(), dirName);
        return getDir(file) ? file : null;
    }

    //-----------------------------------------------------------------------------
    //- Temp dir and file- hoangminh - 10:27 AM - 2/3/16

    public static File getAppTempDir() {
        File file = new File(getAppFile(), "Temp");
        return getDir(file) ? file : null;
    }

    public static File getAppTempFile(final String ext) {
        // Get dir
        File dir = getAppTempDir();
        if (dir == null)
            return null;

        // Create fileName with extension
        String filename = "" + ClockUtils.currentTimeMillis();
        if (ext != null)
            filename = filename + "." + ext;

        return new File(dir, filename);
    }

    public static File getPublicTempDir() {
        File file = new File(getPublicDir(), "Temp");
        return getDir(file) ? file : null;
    }

    public static File getPublicTempFile(final String ext) {
        // Get dir
        File dir = getPublicTempDir();
        if (dir == null)
            return null;

        // Create fileName with extension
        String filename = "" + ClockUtils.currentTimeMillis();
        if (ext != null)
            filename = filename + "." + ext;

        return new File(dir, filename);
    }

    //-----------------------------------------------------------------------------
    // File utils - hoangminh - 9:28 AM - 2/3/16
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    //- Get camera uri - hoangminh - 9:37 AM - 2/3/16

    public static Uri getCameraImageUri() {
        File imageFile = getCameraImageFile();
        if (imageFile == null) {
            LogUtils.e(TAG, "Can not create camera image uri");
            return null;
        }

        return Uri.fromFile(imageFile);
    }

    public static File getCameraImageFile() {
        // Get dir
        File defaultImageDir = getDefaultPublicImageDir();
        if (defaultImageDir == null)
            return null;

        // Create image name
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = simpleDateFormat.format(new Date());
        String imageName = "IMG_" + timeStamp + ".jpg";

        return new File(defaultImageDir, imageName);
    }

    //-----------------------------------------------------------------------------
    //- Uri utils - hoangminh - 9:50 AM - 2/3/16

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String columnName = MediaStore.Images.Media.DATA;
            String[] filePathColumn = {columnName};
            cursor = context.getContentResolver().query(contentUri, filePathColumn, null, null, null);
            if (cursor == null)
                return null;
            int columnIndex = cursor.getColumnIndexOrThrow(columnName);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void deleteUri(Context context, Uri uri) {
        // Sanity check
        if (uri == null)
            return;

        // Delete
        context.getContentResolver().delete(uri, null, null);

        // Refresh gallery data
        Uri galleryUri = Uri.parse("file://" + Environment.getExternalStorageDirectory());
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, galleryUri));
    }

    //-----------------------------------------------------------------------------
    // Delete dir - hoangminh - 10:17 AM - 2/3/16
    //-----------------------------------------------------------------------------

    // Delete everything: files + folders
    public static boolean deleteFolder(File fileOrDirectory) {
        // Sanity check
        if (fileOrDirectory == null || !fileOrDirectory.exists())
            return false;

        // Go to subFolder
        if (fileOrDirectory.isDirectory()) {
            File[] listFiles = fileOrDirectory.listFiles();
            for (File child : listFiles)
                deleteFolder(child);
        }

        // Delete fileOrFolder
        return fileOrDirectory.delete();
    }

    // Only delete direct children files
    public static boolean deleteFilesInsideFolder(File fileOrDirectory) {
        // Sanity check
        if (fileOrDirectory == null || !fileOrDirectory.exists())
            return false;
        if (!fileOrDirectory.isDirectory())
            return false;

        // Delete all files (not folder)
        File[] files = fileOrDirectory.listFiles();
        for (File f : files)
            if (f.isFile())
                f.delete();

        return true;
    }

    //-----------------------------------------------------------------------------
    // File operation - hoangminh - 10:56 AM - 2/3/16
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    //- Copy file - hoangminh - 11:10 AM - 2/3/16

    public static boolean copyFile(File srcFile, File dstFile) {
        // Sanity check
        if (srcFile == null || dstFile == null)
            return false;

        // srcFile not exist or empty
        if (!srcFile.exists() || srcFile.getTotalSpace() <= 0)
            return false;

        // Delete dstFile if exist
        if (dstFile.exists() && !dstFile.delete())
            return false;

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(dstFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean copyFile(byte[] bytes, File dstFile) {
        // Sanity check
        if (dstFile == null || bytes == null)
            return false;

        // Delete dstFile if exist
        if (dstFile.exists() && !dstFile.delete())
            return false;

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(dstFile));
            bos.write(bytes);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------------
    //- Save or update file - hoangminh - 11:10 AM - 2/3/16

    public static boolean saveOrUpdateFile(File file, byte[] bytes) {
        // Sanity check
        if (file == null || bytes == null)
            return false;

        if (file.exists())
            return updateFile(file, bytes);
        else
            return saveFile(file, bytes);
    }

    private static boolean saveFile(File file, byte[] bytes) {
        // Sanity check
        if (file == null || bytes == null)
            return false;

        // Create image dir
        File dir = file.getParentFile();
        if (dir == null || !getDir(dir))
            return false;

        return copyFile(bytes, file);
    }

    private static boolean updateFile(File file, byte[] bytes) {
        // Sanity check
        if (file == null || bytes == null)
            return false;

        // Create temp file
        File tempFile = getAppTempFile(null);
        if (tempFile == null)
            return false;

        // Save bytes to temp file
        if (!saveFile(tempFile, bytes)) {
            tempFile.delete();
            return false;
        }

        // Delete old File + rename tempFile to File
        return file.delete() && tempFile.renameTo(file);
    }

    //-----------------------------------------------------------------------------
    //- Get byte array - hoangminh - 11:13 AM - 2/3/16

    public static byte[] getByteArray(String filePath) {
        // Sanity check
        if (filePath == null)
            return null;

        File file = new File(filePath);
        return getByteArray(file);
    }

    public static byte[] getByteArray(File file) {
        // Sanity check
        if (file == null || !file.exists())
            return null;

        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return out == null ? null : out.toByteArray();
    }

}
