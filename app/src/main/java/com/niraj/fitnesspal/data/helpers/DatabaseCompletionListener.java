package com.niraj.fitnesspal.data.helpers;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.niraj.fitnesspal.controllers.network.NetworkStateReceiver;
import com.niraj.fitnesspal.utils.Result;

import java.util.Map;

/**
 * @author Ritesh Shakya
 */

public class DatabaseCompletionListener
        implements NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = "DatabaseCompletion";
    private boolean isNetworkAvailable = true;

    public DatabaseCompletionListener(Context context) {
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        context.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public <T> void updateChildren(DatabaseReference databaseReference, Result.OnResult onResult, Map<String, Object> childUpdates) {
        if (isNetworkAvailable) {
            databaseReference.updateChildren(childUpdates, (databaseError, databaseReference1) -> {
                if (databaseError == null) {
                    onResult.onResult(Result.success());
                } else {
                    onResult.onResult(Result.error(databaseError.getMessage()));
                }
            });
        } else {
            databaseReference.updateChildren(childUpdates);
            onResult.onResult(Result.success());
        }
    }

    @Override
    public void networkAvailable() {
        isNetworkAvailable = true;
        Log.e(TAG, "networkAvailable: ");
    }

    @Override
    public void networkUnavailable() {
        isNetworkAvailable = false;
        Log.e(TAG, "networkUnavailable: ");
    }

    public <T> void removeChildren(DatabaseReference userSubscriptionRef, Result.OnResult onResult, String id, String DELETED_FLAG) {
        if (isNetworkAvailable) {
            userSubscriptionRef.child(id)
                    .child(DELETED_FLAG)
                    .setValue(true)
                    .addOnCompleteListener(task -> {
                        if (task.isComplete()) {
                            onResult.onResult(Result.success());
                        } else {
                            onResult.onResult(Result.error(task.getException().getMessage()));
                        }
                    });
        } else {
            userSubscriptionRef.child(id).child(DELETED_FLAG).setValue(true);
        }
    }
}
