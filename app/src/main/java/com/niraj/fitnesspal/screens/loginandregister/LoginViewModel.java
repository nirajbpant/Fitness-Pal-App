package com.niraj.fitnesspal.screens.loginandregister;

import androidx.lifecycle.ViewModel;

import com.niraj.fitnesspal.data.session.SessionRepository;
import com.niraj.fitnesspal.utils.Result;

class LoginViewModel extends ViewModel {
    private SessionRepository sessionRepository = SessionRepository.getInstance();

    void loginUser(String email, String password, Result.OnResult onResult) {
        sessionRepository.loginUser(email, password, onResult);
    }

    void registerUser(String firstName, String lastName, String email, String password, Result.OnResult onResult) {
        sessionRepository.registerUser(firstName, lastName, email, password, onResult);
    }
}
