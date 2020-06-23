package com.niraj.fitnesspal.data.food;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.niraj.fitnesspal.data.DatabaseNames;
import com.niraj.fitnesspal.data.helpers.BaseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.niraj.fitnesspal.data.Constant.DATE_FORMAT;

public class FoodEntry implements BaseModel {

    @Expose
    protected String id;
    @SerializedName(DatabaseNames.DELETED_FLAG)
    protected Boolean isDeleted = false;
    @SerializedName(DatabaseNames.FIELD_NAME)
    private String name;
    @SerializedName(DatabaseNames.FIELD_UNIT)
    private String unit;
    @SerializedName(DatabaseNames.FIELD_CALORIES)
    private float calories;
    @SerializedName(DatabaseNames.FIELD_CARBS)
    private float carbs;
    @SerializedName(DatabaseNames.FIELD_PROTEIN)
    private float protein;
    @SerializedName(DatabaseNames.FIELD_FAT)
    private float fat;
    @SerializedName(DatabaseNames.FIELD_ADDED_DATE)
    private Date addedDate;
    @SerializedName(DatabaseNames.FIELD_NO_OF_ITEMS)
    private int noOfItems;

    public FoodEntry(String id, String name, String unit, float calories, float carbs, float protein, float fat, Date addedDate, int noOfItems) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.addedDate = addedDate;
        this.noOfItems = noOfItems;
    }

    public FoodEntry(String name, String unit, float calories, float carbs, float protein, float fat, Date addedDate, int noOfItems) {
        this(null, name, unit, calories, carbs, protein, fat, addedDate, noOfItems);
    }

    public FoodEntry() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public float getCalories() {
        return calories;
    }

    public float getCarbs() {
        return carbs;
    }

    public float getProtein() {
        return protein;
    }

    public float getFat() {
        return fat;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(DatabaseNames.FIELD_NAME, name);
        result.put(DatabaseNames.FIELD_UNIT, unit);
        result.put(DatabaseNames.FIELD_CALORIES, calories);
        result.put(DatabaseNames.FIELD_CARBS, carbs);
        result.put(DatabaseNames.FIELD_PROTEIN, protein);
        result.put(DatabaseNames.FIELD_FAT, fat);
        result.put(DatabaseNames.FIELD_ADDED_DATE, new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(addedDate));
        result.put(DatabaseNames.FIELD_NO_OF_ITEMS, noOfItems);
        result.put(DatabaseNames.DELETED_FLAG, isDeleted);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodEntry foodEntry = (FoodEntry) o;
        return id.equals(foodEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FoodEntry{" +
                "id='" + id + '\'' +
                ", isDeleted=" + isDeleted +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", calories=" + calories +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", fat=" + fat +
                ", addedDate=" + addedDate +
                ", noOfItems=" + noOfItems +
                '}';
    }
}
