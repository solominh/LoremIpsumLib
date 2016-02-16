package com.lorem_ipsum.modules.event_bus;

/**
 * Created by hoangminh on 1/13/16.
 */
public class LoadingDialogEvent {
    public boolean mShouldShowDialog;

    public LoadingDialogEvent(boolean shouldShowDialog) {
        mShouldShowDialog = shouldShowDialog;
    }
}
