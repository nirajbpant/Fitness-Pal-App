package com.niraj.fitnesspal.screens.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.screens.HomeActivity;
import com.niraj.fitnesspal.screens.loginandregister.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mFirebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
