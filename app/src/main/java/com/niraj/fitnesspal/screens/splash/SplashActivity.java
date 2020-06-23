package com.niraj.fitnesspal.screens.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.screens.base.BaseActivity;
import com.niraj.fitnesspal.screens.home.HomeActivity;
import com.niraj.fitnesspal.screens.loginandregister.LoginActivity;

public class SplashActivity extends BaseActivity {
    private SplashViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        showLoading();
        viewModel.isLoggedIn(result -> {
            hideLoading();
            switch (result.getStatus()) {
                case SUCCESS:
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    break;
                case ERROR:
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    break;
            }
            finishAffinity();
        });
    }
}
