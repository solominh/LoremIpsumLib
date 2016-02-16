package com.lorem_ipsum.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.lorem_ipsum.R;
import com.lorem_ipsum.customviews.FullscreenDialog;


/**
 * Created by originally.us on 4/17/14.
 */
public class DialogUtils {

    // Create dialog with custom layout
    public static Dialog createCustomDialogLoading(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogLoading);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_progress_wheel, null);
        dialog.setContentView(v);
        dialog.setCancelable(false);

        return dialog;
    }

    // Create dialog with custom layout
    public static Dialog createFullScreenDialogLoading(Context context) {
        Dialog dialog = new FullscreenDialog(context, R.style.CustomDialogLoading);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_progress_wheel, null);
        dialog.setContentView(v);
        dialog.setCancelable(false);

        return dialog;
    }

    public static void fullscreenMode(final Dialog dialog) {
        // Sanity check
        if (dialog == null)
            return;

        // For android 19 and above
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = dialog.getWindow().getDecorView();
            if (decorView == null)
                return;
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            decorView.setSystemUiVisibility(flags);
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                        fullscreenMode(dialog);
                }
            });
            return;
        }

        // For android below 19
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = dialog.getWindow().getDecorView();
        if (decorView != null)
            decorView.setSystemUiVisibility(View.GONE);
    }



}
