package com.niraj.fitnesspal.screens.splash;

import androidx.lifecycle.ViewModel;

import com.niraj.fitnesspal.data.session.SessionRepository;
import com.niraj.fitnesspal.utils.Result;

public class SplashViewModel extends ViewModel {
    private SessionRepository sessionRepository = SessionRepository.getInstance();

    void isLoggedIn(Result.OnResult onResult) {
        sessionRepository.isLoggedIn(onResult);
    }
}
