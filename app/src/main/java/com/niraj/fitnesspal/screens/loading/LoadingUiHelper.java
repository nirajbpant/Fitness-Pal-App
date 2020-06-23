package com.niraj.fitnesspal.screens.loading;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.niraj.fitnesspal.R;

public class LoadingUiHelper {
    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String TAG = "ProgressDialogFragment";

    private static LoadingUiHelper instance = null;


    public static LoadingUiHelper getInstance() {
        if (instance == null) {
            instance = new LoadingUiHelper();
        }
        return instance;
    }


    public ProgressDialogFragment showProgress(FragmentManager fragmentManager, Type type) {
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TYPE, type);
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(fragmentManager, TAG);

        return progressDialogFragment;
    }

    public enum Type {
        FULL_SCREEN,
        DIALOG
    }

    public static class ProgressDialogFragment extends DialogFragment {

        private Type type = null;

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putSerializable(KEY_TYPE, type);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setCancelable(false);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                type = Type.FULL_SCREEN;
            } else {
                type = (Type) savedInstanceState.getSerializable(KEY_TYPE);
                if (type == null) type = Type.FULL_SCREEN;
            }
            return inflater.inflate(R.layout.fragment_progress_dialog, container, false);
        }

        @Override
        public void onStart() {
            super.onStart();
            if (getDialog() == null) return;
            Window window = getDialog().getWindow();
            assert window != null;
            switch (type) {
                case DIALOG: {
                    Display display = window.getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    window.setLayout((int) (width * 0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);
                    break;
                }
                case FULL_SCREEN: {
                    window.setBackgroundDrawable(null);
                    window.setDimAmount(.05f);
                    break;
                }
            }
        }
    }
}