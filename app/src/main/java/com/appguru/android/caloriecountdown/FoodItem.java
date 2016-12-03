package com.appguru.android.caloriecountdown;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jhani on 11/26/2016.
 */

public class FoodItem  implements Parcelable {

    //constructor
    public FoodItem(){

    }

    private String foodName ;
    private String foodCalories;
    private String foodProtein;
    private String foodCarbs;
    private String foodFat;

    protected FoodItem(Parcel in) {
        foodName = in.readString();
        foodCalories = in.readString();
        foodProtein = in.readString();
        foodCarbs = in.readString();
        foodFat = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCalories() {
        return foodCalories;
    }

    public void setFoodCalories(String foodCalories) {
        this.foodCalories = foodCalories;
    }

    public String getFoodProtein() {
        return foodProtein;
    }

    public void setFoodProtein(String foodProtein) {
        this.foodProtein = foodProtein;
    }

    public String getFoodCarbs() {
        return foodCarbs;
    }

    public void setFoodCarbs(String foodCarbs) {
        this.foodCarbs = foodCarbs;
    }

    public String getFoodFat() {
        return foodFat;
    }

    public void setFoodFat(String foodFat) {
        this.foodFat = foodFat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodName);
        parcel.writeString(foodCalories);
        parcel.writeString(foodProtein);
        parcel.writeString(foodCarbs);
        parcel.writeString(foodFat);
    }
}
