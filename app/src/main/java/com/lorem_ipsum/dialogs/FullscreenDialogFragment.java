package com.lorem_ipsum.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by hoangminh on 10/24/15.
 */
public class FullscreenDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            preventFocus(dialog);
            fullscreenMode(dialog);
        }

        super.onStart();
    }

    private void preventFocus(final Dialog dialog) {
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        });
    }

    public void fullscreenMode(final Dialog dialog) {
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

}
