package com.niraj.fitnesspal.data;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private static final String TAG = SingleLiveEvent.class.getSimpleName();

    private AtomicBoolean pending = new AtomicBoolean(false);


    @Override
    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }
        super.observe(owner, t -> {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    @Override
    public void setValue(T value) {
        pending.set(true);
        super.setValue(value);
    }

    @MainThread
    void call() {
        setValue(null);
    }

    @MainThread
    void callWithValue(T data) {
        setValue(data);
    }
}
