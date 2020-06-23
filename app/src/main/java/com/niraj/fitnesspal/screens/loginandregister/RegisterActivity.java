package com.niraj.fitnesspal.screens.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;
import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.data.User;
import com.niraj.fitnesspal.screens.HomeActivity;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    EditText editFirstName, editLastName, editEmail, editPassword;
    TextInputLayout editEmailLayout, editPasswordLayout, editFirstNameLayout, editLastNameLayout;

    Button btnSignup;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
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

        btnSignup.setOnClickListener(view -> {
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
                mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, task.getException().getMessage(), task.getException());
                            Toast.makeText(RegisterActivity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                        } else {
                            User user = new User(firstName, lastName, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });

        tvSignIn.setOnClickListener(view -> finish());
    }
}
