package com.niraj.fitnesspal.utils;

import static com.niraj.fitnesspal.utils.Result.Status.SUCCESS;

public class Result<T> {
    final Status status;
    final T data;
    final String message;

    private Result(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public static Result success() {
        return new Result<>(SUCCESS, null, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(SUCCESS, data, null);
    }

    public static Result error() {
        return new Result<>(Status.ERROR, null, null);
    }

    public static Result error(String message) {
        return new Result<>(Status.ERROR, null, message);
    }

    public static enum Status {
        SUCCESS,
        ERROR,
    }

    public interface OnResultData<T> {
        void onResult(Result<T> result);
    }

    public interface OnResult {
        void onResult(Result result);
    }
}
