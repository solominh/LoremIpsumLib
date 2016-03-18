package com.lorem_ipsum.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class NetworkStateReceiver extends BroadcastReceiver {

    protected List<NetworkStateReceiverListener> mNetworkStateReceiverListeners;
    protected Boolean mConnected;

    public NetworkStateReceiver() {
        mNetworkStateReceiverListeners = new ArrayList<>();
        mConnected = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            mConnected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
            mConnected = false;
        }

        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(NetworkStateReceiverListener listener : mNetworkStateReceiverListeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if(mConnected == null || listener == null)
            return;

        if(mConnected)
            listener.networkAvailable();
        else
            listener.networkUnavailable();
    }

    //----------------------------------------------------------------------------------------------
    // Interface
    //----------------------------------------------------------------------------------------------

    public void addListener(NetworkStateReceiverListener listener) {
        mNetworkStateReceiverListeners.add(listener);
        notifyState(listener);
    }

    public void removeListener(NetworkStateReceiverListener listener) {
        mNetworkStateReceiverListeners.remove(listener);
    }

    public interface NetworkStateReceiverListener {
        void networkAvailable();
        void networkUnavailable();
    }
}
