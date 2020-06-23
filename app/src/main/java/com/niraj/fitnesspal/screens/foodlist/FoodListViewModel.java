package com.niraj.fitnesspal.screens.foodlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.niraj.fitnesspal.data.food.FoodEntry;
import com.niraj.fitnesspal.data.food.FoodRepository;
import com.niraj.fitnesspal.data.helpers.ActionWrapper;
import com.niraj.fitnesspal.utils.Result;

public class FoodListViewModel extends AndroidViewModel {

    private FoodRepository foodRepository;

    public FoodListViewModel(@NonNull Application application) {
        super(application);
        foodRepository = FoodRepository.getInstance(application);
    }

    void getFoodItems(Result.OnResultData<ActionWrapper<FoodEntry>> onResult) {
        foodRepository.getItems(onResult);
    }
}
