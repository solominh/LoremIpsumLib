package com.lorem_ipsum.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by hoangminh on 12/28/15.
 */
public class FullscreenDialog extends Dialog {

    public FullscreenDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onStart() {
        setFullScreen(this);
        super.onStart();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fullscreenMode(FullscreenDialog.this);
                }
            }, 100);
        }
    }

    private void setFullScreen(final Dialog dialog) {
        fullscreenMode(dialog);
        preventFocusChange(dialog);
    }

    public static void fullscreenMode(final Dialog dialog) {
        if (dialog == null)
            return;

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

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = dialog.getWindow().getDecorView();
        if (decorView != null)
            decorView.setSystemUiVisibility(View.GONE);
    }

    private void preventFocusChange(final Dialog dialog) {
        if (dialog == null)
            return;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        });
    }

}
