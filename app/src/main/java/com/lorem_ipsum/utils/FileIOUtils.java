package com.lorem_ipsum.utils;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by hoangminh on 2/3/16.
 */
public class FileIOUtils {

    private static final String TAG = "FileIOUtils";

    //-----------------------------------------------------------------------------
    // Read text file - hoangminh - 10:13 AM - 2/3/16
    //-----------------------------------------------------------------------------

    public static String readTextFile(AssetManager assetManager, String filePath) {
        // Sanity check
        if (assetManager == null || filePath == null)
            return null;

        InputStream is = null;
        try {
            is = assetManager.open(filePath);
            return readTextFile(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String readTextFile(String filePath) {
        // Sanity check
        if (filePath == null)
            return null;

        try {
            InputStream is = new FileInputStream(filePath);
            return readTextFile(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String readTextFile(InputStream is) {
        // Sanity check
        if (is == null)
            return null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));

            String str = "";
            StringBuilder buf = new StringBuilder();
            str = reader.readLine();
            while (str != null) {
                buf.append(str).append("\n");
                str = reader.readLine();
            }

            return buf.toString();

        } catch (FileNotFoundException e) {
            LogUtils.d(TAG, "readTextFile() File not found: " + e.getMessage());
        } catch (IOException e) {
            LogUtils.d(TAG, "readTextFile() IO error: " + e.getMessage());
        } finally {
            // Close reader
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //-----------------------------------------------------------------------------
    // Unzip file - hoangminh - 10:13 AM - 2/3/16
    //-----------------------------------------------------------------------------

    public static void unZip(String zipFilePath, String unZipPath) {
        //Sanity check
        if (!unZipPath.endsWith("/")) {
            unZipPath += "/";
        }

        ZipInputStream zin = null;
        try {
            FileInputStream fin = new FileInputStream(zipFilePath);
            zin = new ZipInputStream(fin);
            ZipEntry entry;

            //Read entries in zip file
            while ((entry = zin.getNextEntry()) != null) {
                LogUtils.d(TAG, "unzipping " + entry.getName() + "  length " + entry.getSize());

                if (entry.isDirectory()) {
                    File f = new File(unZipPath + entry.getName());
                    if (!f.isDirectory())
                        f.mkdirs();

                } else {
                    //Write this entry to file
                    FileOutputStream fout = new FileOutputStream(unZipPath + entry.getName());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zin.read(buffer)) != -1) {
                        fout.write(buffer, 0, length);
                    }

                    fout.flush();
                    fout.close();
                    zin.closeEntry();
                }
            }

        } catch (Exception e) {
            LogUtils.e(TAG, "unzip error: " + e.getMessage());

        } finally {
            try {
                if (zin != null)
                    zin.close();
            } catch (IOException e) {
                LogUtils.e(TAG, "unzip error: " + e.getMessage());
            }
        }
    }

}
