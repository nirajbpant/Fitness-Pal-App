package com.niraj.fitnesspal.screens.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.screens.base.BaseActivity;
import com.niraj.fitnesspal.screens.home.HomeActivity;

public class LoginActivity extends BaseActivity {
    private EditText editEmail, editPassword;
    private TextInputLayout editEmailLayout, editPasswordLayout;
    private Button loginBtn;
    private TextView tvSignup;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        editEmail = findViewById(R.id.editEmail);
        editEmailLayout = findViewById(R.id.editEmailLayout);
        editPassword = findViewById(R.id.editPassword);
        editPasswordLayout = findViewById(R.id.editPasswordLayout);
        loginBtn = findViewById(R.id.loginBtn);
        tvSignup = findViewById(R.id.tvSignup);

        loginBtn.setOnClickListener(view -> verifyAndLogin());

        tvSignup.setOnClickListener(view -> navigateToRegister());
    }

    private void navigateToRegister() {
        Intent intToSignup = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intToSignup);
    }

    private void verifyAndLogin() {
        String email = editEmail.getText().toString();
        String pass = editPassword.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailLayout.setError("Please enter email");
            editEmail.requestFocus();
        } else if (pass.isEmpty()) {
            editPasswordLayout.setError("Please enter password");
            editPassword.requestFocus();
        } else {
            loginUser(email, pass);
        }
    }

    private void loginUser(String email, String pass) {
        showLoading();
        viewModel.loginUser(email, pass, result -> {
            hideLoading();
            switch (result.getStatus()) {
                case SUCCESS:
                    Intent intentToHome = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intentToHome);
                    break;
                case ERROR:
                    showToast(result.getMessage());
                    break;
            }
        });
    }

}
