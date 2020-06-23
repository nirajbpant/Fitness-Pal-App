package com.example.fitnesspal.screens.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesspal.R;
import com.example.fitnesspal.screens.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity  extends AppCompatActivity{
    EditText editEmail, editPassword;
    Button loginBtn;
    TextView tvSignup;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mFirebaseAuth= FirebaseAuth.getInstance();
        editEmail= findViewById(R.id.editEmail);
        editPassword= findViewById(R.id.editPassword);
        loginBtn= findViewById(R.id.loginBtn);
        tvSignup= findViewById(R.id.tvSignup);

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser= mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!= null){
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT);
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid Login Credentials", Toast.LENGTH_SHORT);
                }
            }
        };
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= editEmail.getText().toString();
                String pass= editPassword.getText().toString();
                if(email.isEmpty()){
                    editEmail.setError("Please enter email");
                    editEmail.requestFocus();
                }else if(pass.isEmpty()){
                    editPassword.setError("Please enter password");
                    editPassword.requestFocus();
                }else if(email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please fill login credential",Toast.LENGTH_SHORT);
                }else if(!(email.isEmpty()) && !(pass.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT);
                            }else{
                                Intent intentToHome= new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intentToHome);
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Error Occurred", Toast.LENGTH_SHORT);
                }
            }
        });
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToSignup= new Intent(LoginActivity.this, RegisterActivity.class);
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
