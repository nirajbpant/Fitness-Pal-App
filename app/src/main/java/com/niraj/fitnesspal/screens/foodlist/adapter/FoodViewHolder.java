package com.niraj.fitnesspal.screens.foodlist.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.data.food.FoodEntry;

class FoodViewHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private TextView headingTxt, unitTxt, countTxt, subtitleTxt;

    FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        headingTxt = itemView.findViewById(R.id.headingTxt);
        unitTxt = itemView.findViewById(R.id.unitTxt);
        countTxt = itemView.findViewById(R.id.countTxt);
        subtitleTxt = itemView.findViewById(R.id.subtitleTxt);
    }

    void bind(FoodEntry entry) {
        headingTxt.setText(entry.getName());
        unitTxt.setText(entry.getUnit());
        countTxt.setText(String.format(context.getString(R.string.x_format), entry.getNoOfItems()));
        subtitleTxt.setText(String.format(context.getString(R.string.food_item_detail_list), entry.getCalories(), entry.getCarbs(), entry.getProtein(), entry.getFat()));
    }
}
