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

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText editFirstName, editLastName, editEmail, editPassword;
    private TextInputLayout editEmailLayout, editPasswordLayout, editFirstNameLayout, editLastNameLayout;

    private Button btnSignup;
    private TextView tvSignIn;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        editEmail = findViewById(R.id.editEmail);
        editEmailLayout = findViewById(R.id.editEmailLayout);
        editFirstName = findViewById(R.id.editFirstName);
        editFirstNameLayout = findViewById(R.id.editFirstNameLayout);
        editLastName = findViewById(R.id.editLastName);
        editLastNameLayout = findViewById(R.id.editLastNameLayout);
        editPassword = findViewById(R.id.editPassword);
        editPasswordLayout = findViewById(R.id.editPasswordLayout);
        tvSignIn = findViewById(R.id.tvSignIn);
        btnSignup = findViewById(R.id.registerBtn);

        btnSignup.setOnClickListener(view -> validateAndRegister());

        tvSignIn.setOnClickListener(view -> finish());
    }

    private void validateAndRegister() {
        final String firstName = editFirstName.getText().toString();
        final String lastName = editLastName.getText().toString();
        final String email = editEmail.getText().toString();
        final String pass = editPassword.getText().toString();
        if (firstName.isEmpty()) {
            editFirstNameLayout.setError("Please provide firstName");
            editFirstName.requestFocus();
        } else if (lastName.isEmpty()) {
            editLastNameLayout.setError("Please provide lastName");
            editLastName.requestFocus();
        } else if (email.isEmpty()) {
            editEmailLayout.setError("Please enter email");
            editEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailLayout.setError("Invalid Email Address");
            editEmail.requestFocus();
        } else if (pass.isEmpty()) {
            editPasswordLayout.setError("Please enter password");
            editPassword.requestFocus();
        } else {
            registerUser(firstName, lastName, email, pass);
        }
    }


    private void registerUser(String firstName, String lastName, String email, String pass) {
        showLoading();
        viewModel.registerUser(firstName, lastName, email, pass, result -> {
            hideLoading();
            switch (result.getStatus()) {
                case SUCCESS:
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finishAffinity();
                    break;
                case ERROR:
                    showToast(result.getMessage());
                    break;
            }
        });
    }
}
