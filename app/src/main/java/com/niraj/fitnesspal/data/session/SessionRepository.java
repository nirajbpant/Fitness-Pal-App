package com.niraj.fitnesspal.data.session;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.niraj.fitnesspal.data.User;
import com.niraj.fitnesspal.utils.Result;

public class SessionRepository {
    private static SessionRepository instance = null;
    private final String USERS_DATABASE = "Users";

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    public static SessionRepository getInstance() {
        if (instance == null) {
            instance = new SessionRepository();
        }
        return instance;
    }

    public void isLoggedIn(Result.OnResult onResult) {
        mFirebaseAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                onResult.onResult(Result.success());
            } else {
                onResult.onResult(Result.error());
            }
        });
    }

    public void loginUser(String email, String password, Result.OnResult onResult) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                onResult.onResult(Result.error("Invalid Credentials"));
            } else {
                onResult.onResult(Result.success());
            }
        });
    }

    public void logoutUser(Result.OnResult onResult) {
        mFirebaseAuth.signOut();
        onResult.onResult(Result.success());
    }

    public void registerUser(String firstName, String lastName, String email, String password, Result.OnResult onResult) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                onResult.onResult(Result.error("SignUp Unsuccessful"));
            } else {
                User user = new User(firstName, lastName, email);
                FirebaseDatabase.getInstance().getReference(USERS_DATABASE)
                        .child(mFirebaseAuth.getCurrentUser().getUid())
                        .setValue(user).addOnCompleteListener(createUserTask -> {
                    if (createUserTask.isSuccessful()) {
                        onResult.onResult(Result.success());
                    } else {
                        onResult.onResult(Result.error("SignUp Unsuccessful"));
                    }
                });
            }
        });
    }
}
