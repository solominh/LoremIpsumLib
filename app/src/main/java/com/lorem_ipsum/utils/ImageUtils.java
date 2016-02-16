package com.lorem_ipsum.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by originally.us on 4/17/14.
 * <p>
 * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 */
public class ImageUtils {

    private static final String LOG_TAG = "ImageUtils";

    //-----------------------------------------------------------------------------
    //- Decode image - hoangminh - 10:30 AM - 2/1/16

    public static Bitmap decodeBitmapFromImageUri(Context context, Uri imageUri, int reqWidth, int reqHeight) {
        // Sanity check
        if (imageUri == null)
            return null;

        // Get image path from image uri
        String imagePath = FileUtils.getRealPathFromURI(context, imageUri);

        // Decode image path
        return StringUtils.isNull(imagePath) ? null : decodeBitmapFromImagePath(imagePath, reqWidth, reqHeight);
    }

    public static Bitmap decodeBitmapFromImagePath(String imagePath, int reqWidth, int reqHeight) {
        // Sanity check
        if (StringUtils.isNull(imagePath))
            return null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false; //Nexus 7 auto scale when decode resource, use inScale = false
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap sourceBitmap = BitmapFactory.decodeFile(imagePath, options);
        if (sourceBitmap == null)
            return null;

        // Fix image rotation
        int rotationRadius = getRotationRadius(imagePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationRadius);

        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //Nexus 7 auto scale when decode resource, use inScale = false
        options.inScaled = false;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //-----------------------------------------------------------------------------
    //- Utils - hoangminh - 10:22 AM - 2/1/16

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Sanity check
        if (reqWidth <= 0 && reqHeight <= 0)
            return 1;

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((height / inSampleSize) > reqHeight && (width / inSampleSize) > reqWidth)
            inSampleSize *= 2;

        return inSampleSize / 2;
    }

    // Check orientation and return radius of rotation
    private static int getRotationRadius(String imagePath) {
        // Get image exif
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            Log.w(LOG_TAG, "getRotationRadius error: " + e.getMessage());
        }

        if (exif == null)
            return 0;

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        int rotationRadius = 0;
        switch (orientation) {
            case 3:
                rotationRadius = 180;
                break;
            case 6:
                rotationRadius = 90;
                break;
            case 8:
                rotationRadius = -90;
                break;
            default:
                rotationRadius = 0;
                break;
        }
        return rotationRadius;
    }

    /**
     * Resize
     */

    public static Uri resizeImage(Context context, Uri imageUri, int maxPixel) {
        // Get bitmap
        Bitmap sourceBitmap = decodeBitmapFromImageUri(context, imageUri, maxPixel, maxPixel);
        if (sourceBitmap == null)
            return null;

        // Get the original width and height
        int inputWidth = sourceBitmap.getWidth();
        int inputHeight = sourceBitmap.getHeight();
        if (inputWidth <= maxPixel && inputHeight <= maxPixel)
            return imageUri;

        // Get new dimen after scale
        Point newDimen = getScaleDimen(inputWidth, inputHeight, maxPixel);
        Bitmap dstBitmap = Bitmap.createScaledBitmap(sourceBitmap, newDimen.x, newDimen.y, false);

        // Save scale bitmap
        return saveImageFile(context, dstBitmap);
    }

    private static Point getScaleDimen(int inputWidth, int inputHeight, int maxPixel) {
        if (inputWidth <= maxPixel && inputHeight <= maxPixel)
            return new Point(inputWidth, inputHeight);

        int outputWidth, outputHeight;
        float widthHeightRatio = inputWidth / inputHeight;

        if (inputWidth <= inputHeight) {
            outputHeight = maxPixel;
            outputWidth = (int) (outputHeight * widthHeightRatio);
        } else {
            outputWidth = maxPixel;
            outputHeight = (int) (outputWidth / widthHeightRatio);
        }

        return new Point(outputWidth, outputHeight);
    }

    //-----------------------------------------------------------------------------
    // Image Operation - hoangminh - 11:24 AM - 2/3/16
    //-----------------------------------------------------------------------------

    public static boolean saveImageAsPNG(Bitmap bitmap, String imgPath) {
        // Sanity check
        if (imgPath == null || bitmap == null)
            return false;

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imgPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean saveImageAsJPG(Bitmap bitmap, String imgPath) {
        // Sanity check
        if (imgPath == null || bitmap == null)
            return false;

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imgPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private static Uri saveImageFile(Context context, Bitmap bitmap) {
        File pictureFile = FileUtils.getCameraImageFile();
        if (pictureFile == null) {
            LogUtils.d(LOG_TAG, "Error creating media file, check storage permissions");
            return null;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            return getImageContentUri(context, pictureFile);
        } catch (FileNotFoundException e) {
            LogUtils.d(LOG_TAG, "File not found: " + e.getMessage());
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    LogUtils.d(LOG_TAG, "Error accessing file: " + e.getMessage());
                    e.printStackTrace();
                }
        }

        return null;
    }

    /**
     * get uri format: content://media/external/images/media/xxxxx
     */

    private static Uri getImageContentUri(Context context, File imageFile) {

        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");

            return Uri.withAppendedPath(baseUri, "" + id);

        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Crop normal bitmap to square bitmap and rotate
     */

    public static Uri saveBitmapWithOptimizeRotation(Context context, Bitmap sourceBitmap, float degreesRotate) {
        if (sourceBitmap == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degreesRotate);

        Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
        return ImageUtils.saveImageFile(context, bitmap);
    }

    public static Uri saveBitmapWithOptimizeRotation(Context context, byte[] bitmapData, float degreesRotate) {
        if (bitmapData == null)
            return null;
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
        return saveBitmapWithOptimizeRotation(context, originalBitmap, degreesRotate);
    }

    public static boolean isPicasaImage(Uri imageUri) {
        if (imageUri.toString().startsWith("content://com.google.android.gallery3d")) // picasa
            return true;
        if (imageUri.toString().startsWith("content://com.google.android.apps.photos")) // photo in google plus)
            return true;
        return false;
    }

    public interface OnDownloadExternalImageDone {
        void onDone(Uri imageUri);
    }

    public static void downloadExternalImageInBackground(final Activity context, final Uri imageUri, final OnDownloadExternalImageDone callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // create directory
                File cacheDir;
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                    cacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); // If the device has an SD card
                else
                    cacheDir = context.getApplicationContext().getCacheDir(); // If no SD card
                if (!cacheDir.exists())
                    cacheDir.mkdirs();

                // create file
                final String path = "gush-" + System.currentTimeMillis() + ".jpg";
                File file = new File(cacheDir, path);

                try {
                    InputStream input;
                    // Download the file
                    if (ImageUtils.isPicasaImage(imageUri)) {
                        input = context.getContentResolver().openInputStream(imageUri);
                    } else {
                        input = new URL(imageUri.toString()).openStream();
                    }

                    OutputStream output = new FileOutputStream(file);
                    byte data[] = new byte[1024];
                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();

                    final Uri newUri = getImageContentUri(context, file);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(newUri);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDone(null);
                    }
                });
            }
        }).start();
    }

    //-----------------------------------------------------------------------------
    // Take screen shot - hoangminh - 3:57 PM - 2/1/16
    //-----------------------------------------------------------------------------

    public static Bitmap takeScreenShot(View view) {
        if (view == null)
            return null;

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap invertBitmap(Bitmap bm) {
        // Get color filter
        ColorMatrix colorMatrix_Inverted = new ColorMatrix(new float[]{
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0});
        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(colorMatrix_Inverted);

        // Get paint
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);

        // Draw bitmap
        Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bm, 0, 0, paint);

        return bitmap;
    }

    //-----------------------------------------------------------------------------
    // Check image extension - hoangminh - 11:27 AM - 2/3/16
    //-----------------------------------------------------------------------------

    public static boolean isGif(String url) {
        return url != null && url.toLowerCase().endsWith(".gif");
    }

    //-----------------------------------------------------------------------------
    // Image exif interface - hoangminh - 11:28 AM - 2/3/16
    //-----------------------------------------------------------------------------

    // EXIF FOR JPEG IMAGES (PNG DOESN'T HAVE EXIF METADATA).
    // We can also use tag: "UserComment"

    public static String getExif(String imgPath) {
        if (imgPath == null)
            return null;
        try {
            ExifInterface exif = new ExifInterface(imgPath);
            return exif.getAttribute(ExifInterface.TAG_MAKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setExif(String imgPath, String imgBound) {
        if (imgPath == null || imgBound == null)
            return;
        try {
            ExifInterface exif = new ExifInterface(imgPath);
            exif.setAttribute(ExifInterface.TAG_MAKE, imgBound);
            exif.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------
    // Encode image file to string - hoangminh - 1:08 PM - 2/3/16
    //-----------------------------------------------------------------------------

    public static String encodeImage(String filePath) {
        String encodedFile = null;
        try {
            // Get bytes from file
            byte[] bytes = FileUtils.getByteArray(filePath);
            if (bytes == null)
                return null;

            // Encode bytes to string
            encodedFile = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedFile;
    }

    public static byte[] decodeImage(String encodedFile) {
        byte[] bytes = null;
        try {
            bytes = Base64.decode(encodedFile, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }


}
