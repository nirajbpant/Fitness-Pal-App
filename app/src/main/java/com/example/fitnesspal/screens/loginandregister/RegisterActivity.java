package com.example.fitnesspal.screens.loginandregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesspal.R;
import com.example.fitnesspal.data.User;
import com.example.fitnesspal.screens.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText editFirstName, editLastName, editEmail, editPassword;
    EditText editDob;
    Button btnSignup;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mFirebaseAuth= FirebaseAuth.getInstance();
        editEmail= findViewById(R.id.editEmail);
        editFirstName= findViewById(R.id.editFirstName);
        editLastName= findViewById(R.id.editLastName);
        editPassword= findViewById(R.id.editPassword);
        editDob= findViewById(R.id.editDob);
        tvSignIn= findViewById(R.id.tvSignIn);
        btnSignup= findViewById(R.id.registerBtn);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName= editFirstName.getText().toString();
                final String lastName= editLastName.getText().toString();
                final String email= editEmail.getText().toString();
                String pass= editPassword.getText().toString();
                final String dob= editDob.getText().toString();
                if(firstName.isEmpty()){
                    editFirstName.setError("Please provide firstName");
                    editFirstName.requestFocus();
                }else if(lastName.isEmpty()){
                    editLastName.setError("Please provide lastName");
                    editLastName.requestFocus();
                }else if(email.isEmpty()){
                    editEmail.setError("Please enter email");
                    editEmail.requestFocus();
                }else if(pass.isEmpty()){
                    editPassword.setError("Please enter password");
                    editPassword.requestFocus();
                }else if(dob.isEmpty()){
                    editDob.setError("Please enter your date of birth");
                    editDob.requestFocus();
                }else if(firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && pass.isEmpty() && dob.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Fields are empty", Toast.LENGTH_SHORT);
                }else if(!(firstName.isEmpty()) && !(lastName.isEmpty()) && !(email.isEmpty()) && !(pass.isEmpty()) && !(dob.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                User user= new User(firstName,lastName,email,dob);
                                FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT);
                                    }else{
                                            //Any message
                                        }
                                    }
                                });
                                Toast.makeText(RegisterActivity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT);
                            }else{
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Error Occurred", Toast.LENGTH_SHORT);
                }
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mFirebaseAuth.getCurrentUser()!= null){

        }
    }
}
