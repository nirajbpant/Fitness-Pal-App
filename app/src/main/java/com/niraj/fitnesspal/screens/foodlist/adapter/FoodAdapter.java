package com.niraj.fitnesspal.screens.foodlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.data.food.FoodEntry;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    private List<FoodEntry> items = new ArrayList<>();

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodEntry entry = items.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(FoodEntry data) {
        if (!items.contains(data)) {
            int count = this.items.size();
            items.add(data);
            this.notifyItemInserted(count);
        } else {
            updateItem(data);
        }
    }

    public void updateItem(FoodEntry data) {
        if (items.contains(data)) {
            int index = items.indexOf(data);
            this.items.set(index, data);
            this.notifyItemChanged(index);
        } else {
            addItem(data);
        }
    }

    public void removeItem(FoodEntry data) {
        if (items.contains(data)) {
            int index = items.indexOf(data);
            this.items.remove(data);
            this.notifyItemRemoved(index);
        }
    }

    public List<FoodEntry> getItems() {
        return items;
    }

}
