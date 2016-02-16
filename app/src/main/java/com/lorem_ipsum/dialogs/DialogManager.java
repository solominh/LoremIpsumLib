package com.lorem_ipsum.dialogs;

import android.app.Dialog;
import android.content.Context;

import com.lorem_ipsum.R;
import com.lorem_ipsum.utils.AnimationUtils;
import com.lorem_ipsum.utils.DialogUtils;

/**
 * Created by hoangminh on 1/7/16.
 */
public class DialogManager {
    private Dialog mLoadingDialog;

    private static DialogManager instance = null;

    private DialogManager(){
    }

    public static DialogManager getInstance(){
        if(instance == null){
            instance=new DialogManager();
        }
        return instance;
    }

    public void showLoadingDialog(Context baseContext) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            return;
        if (mLoadingDialog == null)
            mLoadingDialog = DialogUtils.createFullScreenDialogLoading(baseContext);
        mLoadingDialog.show();
        AnimationUtils.AnimationWheelForDialog(baseContext, mLoadingDialog.findViewById(R.id.loading_progress_wheel_view_container));
    }

    public boolean isShowLoadingDialog() {
        return mLoadingDialog != null && mLoadingDialog.isShowing();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog == null)
            return;
        try {
            mLoadingDialog.dismiss();
        } catch (Exception e) {
            // dismiss dialog after destroy activity
        }
    }
}
