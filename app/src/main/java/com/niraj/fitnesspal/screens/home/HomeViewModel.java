package com.niraj.fitnesspal.screens.home;

import androidx.lifecycle.ViewModel;

import com.niraj.fitnesspal.data.session.SessionRepository;
import com.niraj.fitnesspal.utils.Result;

class HomeViewModel extends ViewModel {
    private SessionRepository sessionRepository = SessionRepository.getInstance();

    void logoutUser(Result.OnResult onResult) {
        sessionRepository.logoutUser(onResult);
    }
}
