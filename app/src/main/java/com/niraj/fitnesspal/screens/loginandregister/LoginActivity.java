package com.niraj.fitnesspal.screens.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.screens.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    EditText editEmail, editPassword;
    TextInputLayout editEmailLayout, editPasswordLayout;
    Button loginBtn;
    TextView tvSignup;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.editEmail);
        editEmailLayout = findViewById(R.id.editEmailLayout);
        editPassword = findViewById(R.id.editPassword);
        editPasswordLayout = findViewById(R.id.editPasswordLayout);
        loginBtn = findViewById(R.id.loginBtn);
        tvSignup = findViewById(R.id.tvSignup);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String pass = editPassword.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editEmailLayout.setError("Please enter email");
                    editEmail.requestFocus();
                } else if (pass.isEmpty()) {
                    editPasswordLayout.setError("Please enter password");
                    editPassword.requestFocus();
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intentToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intentToHome);
                            }
                        }
                    });
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToSignup = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intToSignup);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
