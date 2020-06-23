package com.niraj.fitnesspal.screens.base;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.niraj.fitnesspal.screens.loading.LoadingUiHelper;

public abstract class BaseActivity extends AppCompatActivity {
    private LoadingUiHelper.ProgressDialogFragment progressDialogFragment;

    protected void showLoading() {
        if (progressDialogFragment == null) {
            progressDialogFragment = LoadingUiHelper.getInstance().showProgress(getSupportFragmentManager(), LoadingUiHelper.Type.FULL_SCREEN);
        }
    }

    protected void hideLoading() {
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissAllowingStateLoss();
            progressDialogFragment = null;
        }
    }

    protected void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
