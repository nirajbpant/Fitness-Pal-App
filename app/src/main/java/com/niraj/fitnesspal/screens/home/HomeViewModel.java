package com.niraj.fitnesspal.screens.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.niraj.fitnesspal.data.food.FoodEntry;
import com.niraj.fitnesspal.data.food.FoodRepository;
import com.niraj.fitnesspal.data.session.SessionRepository;
import com.niraj.fitnesspal.screens.helper.Classifier;
import com.niraj.fitnesspal.utils.Result;

import java.util.Date;

public class HomeViewModel extends AndroidViewModel {
    private SessionRepository sessionRepository = SessionRepository.getInstance();

    private FoodRepository foodRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        foodRepository = FoodRepository.getInstance(application);
    }

    void logoutUser(Result.OnResult onResult) {
        sessionRepository.logoutUser(onResult);
    }

    void addFoodItem(Classifier.FoodItem foodItem, int noOfItems, Result.OnResult onResult) {
        FoodEntry entry = new FoodEntry(foodItem.getTitle(), foodItem.getItemUnit(), foodItem.getCalories(),
                foodItem.getCarb(), foodItem.getProtein(), foodItem.getFat(), new Date(), noOfItems);
        foodRepository.createOrUpdateItem(entry, onResult);
    }

}
