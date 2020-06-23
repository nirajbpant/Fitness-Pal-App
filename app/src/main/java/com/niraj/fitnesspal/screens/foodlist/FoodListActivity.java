package com.niraj.fitnesspal.screens.foodlist;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.data.food.FoodEntry;
import com.niraj.fitnesspal.data.helpers.ActionWrapper;
import com.niraj.fitnesspal.screens.foodlist.adapter.FoodAdapter;
import com.niraj.fitnesspal.utils.Result;

public class FoodListActivity extends AppCompatActivity {
    private static final String TAG = FoodListActivity.class.getSimpleName();
    private RecyclerView foodsList;
    private Toolbar toolbar;
    private FoodListViewModel viewModel;
    private FoodAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        viewModel = new ViewModelProvider(this).get(FoodListViewModel.class);

        foodsList = findViewById(R.id.foodsList);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        initializeList();
        viewModel.getFoodItems(result -> {
            ActionWrapper<FoodEntry> foodEntry = result.getData();
            Log.e(TAG, foodEntry.toString());
            switch (foodEntry.getAction()) {
                case ADDED:
                    mAdapter.addItem(foodEntry.getData());
                    break;
                case UPDATED:
                    mAdapter.updateItem(foodEntry.getData());
                    break;
                case REMOVED:
                    mAdapter.removeItem(foodEntry.getData());
                    break;
            }
            float itemCount = 0f;
            for (FoodEntry entry : mAdapter.getItems()) {
                itemCount += (entry.getCalories() * entry.getNoOfItems());
            }
            setCalorieCount(itemCount);
        });
        setCalorieCount(0f);
    }

    private void initializeList() {
        foodsList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FoodAdapter();
        foodsList.setAdapter(mAdapter);
    }

    private void setCalorieCount(float calories) {
        String title = String.format(getString(R.string.foods_format), calories);
        toolbar.setTitle(title);
    }


}
