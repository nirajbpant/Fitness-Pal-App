package com.niraj.fitnesspal.data.helpers;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.niraj.fitnesspal.data.Constant;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

/**
 * @author Ritesh Shakya
 */

@SuppressWarnings("unchecked")
public abstract class FirebaseChildListener<C extends BaseModel>
        implements ChildEventListener {
    private static final String TAG = "FirebaseChildListener";
    private final Class<C> persistentClass;

    protected FirebaseChildListener() {
        this.persistentClass = (Class<C>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        onChildAdded(publishEvent("onChildAdded: ", dataSnapshot));
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        onChildChanged(publishEvent("onChildChanged: ", dataSnapshot));
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        onChildRemoved(publishEvent("onChildRemoved: ", dataSnapshot));
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        onChildMoved(publishEvent("onChildMoved: ", dataSnapshot), s);
    }

    private C publishEvent(String msg, DataSnapshot dataSnapshot) {
        HashMap<String, JSONObject> dataSnapshotValue =
                (HashMap<String, JSONObject>) dataSnapshot.getValue();
        String jsonString = new Gson().toJson(dataSnapshotValue);
        final Gson gson = new GsonBuilder().setDateFormat(Constant.DATE_FORMAT).create();
        C result = gson.fromJson(jsonString, persistentClass);
        result.setId(dataSnapshot.getKey());
        Log.i(TAG, msg + result);
        return result;
    }

    public abstract void onChildAdded(C snapShot);

    public abstract void onChildChanged(C snapShot);

    public abstract void onChildRemoved(C snapShot);

    @SuppressWarnings({"UnusedParameters", "EmptyMethod"})
    private void onChildMoved(C snapShot, String s) {
    }
}
