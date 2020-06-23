package com.niraj.fitnesspal.controllers.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ritesh Shakya
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    private final Set<NetworkStateReceiverListener> listeners;
    private Boolean connected;

    public NetworkStateReceiver() {
        listeners = new HashSet<>();
        connected = null;
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null) return;

        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (manager != null) {
            ni = manager.getActiveNetworkInfo();
        }

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                Boolean.FALSE)) {
            connected = false;
        }

        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for (NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if (connected == null || listener == null) return;

        if (connected) {
            listener.networkAvailable();
        } else {
            listener.networkUnavailable();
        }
    }

    public void addListener(NetworkStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public interface NetworkStateReceiverListener {
        void networkAvailable();

        void networkUnavailable();
    }
}