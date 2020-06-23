package com.niraj.fitnesspal.data.helpers;

import androidx.annotation.NonNull;

public class ActionWrapper<T> {
    private final T data;
    private final Action mAction;

    public ActionWrapper(@NonNull T data, @NonNull Action action) {
        this.data = data;
        this.mAction = action;
    }

    public T getData() {
        return data;
    }

    public Action getAction() {
        return mAction;
    }


    public enum Action {
        ADDED, UPDATED, REMOVED
    }

    @Override
    public String toString() {
        return "ActionWrapper{" +
                "data=" + data +
                ", mAction=" + mAction +
                '}';
    }
}

