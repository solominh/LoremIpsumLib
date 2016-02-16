package com.lorem_ipsum.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by originally.us on 4/15/14.
 */
public class DeviceUtils {

    private static final String LOG_TAG = "DeviceUtils";

    //-----------------------------------------------------------------------------
    //- Keyboard - hoangminh - 3:01 PM - 2/1/16

    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static boolean isKeyboardVisible(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    public interface softKeyBoardListener {
        void onSoftKeyBoardHidden();

        void onSoftKeyBoardShowing(int h);
    }

    public static void detectSoftKeyBoard(Context context, final View rootView, final softKeyBoardListener listener) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //rect will be populated with the coordinates of your view that area still visible.
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);

                int heightDiff = rootView.getRootView().getHeight() - (rect.bottom - rect.top);
                if (heightDiff > 100) // if more than 100 pixels, its probably a keyboard
                    listener.onSoftKeyBoardShowing(heightDiff); // keyboard is showing

                else listener.onSoftKeyBoardHidden();  // keyboard is hidden
            }
        });
    }

    //-----------------------------------------------------------------------------
    //- Network - hoangminh - 3:12 PM - 2/1/16

    public static boolean checkNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean checkNetworkState() {
        Context context = AppUtils.getAppContext();
        return checkNetworkState(context);
    }

    //-----------------------------------------------------------------------------
    //- Device dimen - hoangminh - 3:07 PM - 2/1/16

    public static Point getDeviceDimen(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getDeviceScreenWidth(Context context) {
        return getDeviceDimen(context).x;
    }

    public static int getDeviceScreenHeight(Context context) {
        return getDeviceDimen(context).y;
    }

    public static int getActionbarHeight(Context context) {
        int actionBarHeight = 0;
        try {
            final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            actionBarHeight = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
        } catch (Exception e) {
            return actionBarHeight;
        }

        return actionBarHeight;
    }

    //-----------------------------------------------------------------------------
    //- Device id  - hoangminh - 3:07 PM - 2/1/16

    public static String getDeviceUUID(Context context) {
        String id;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        id = tm.getDeviceId();

        if (id == null || id.isEmpty()) {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }

    public static Number getDeviceVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    //-----------------------------------------------------------------------------
    //- Other - hoangminh - 3:10 PM - 2/1/16

    public static boolean isAndroidEmulator() {
        String product = Build.PRODUCT;
        if (product == null)
            return false;
        LogUtils.d(LOG_TAG, "product=" + product);
        return product.matches(".*_?sdk_?.*");
    }

    public static String getDeviceArchitecture() {
        return System.getProperty("os.arch");
    }

    //-----------------------------------------------------------------------------
    //- Check tablet - hoangminh - 3:22 PM - 2/1/16

    public static boolean isTablet(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return (config.smallestScreenWidthDp >= 600);
    }

    public static void requestScreenOrientation(Activity activity) {
        if (isTablet(activity))
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        else
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }
}
