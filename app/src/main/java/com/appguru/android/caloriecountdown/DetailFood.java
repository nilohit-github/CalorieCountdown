package com.appguru.android.caloriecountdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class DetailFood extends AppCompatActivity {

    private String username;
    private String foodSearched;
    FoodAdapter foodAdapter;
    private ListView mFoodListView = null;
    public ArrayList<FoodItem> foodItemArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        Intent intent = getIntent();
        username= intent.getStringExtra("username");
        foodSearched = intent.getStringExtra("fooditem");


        if(username != null && foodSearched != null )
        {
            FetchFood fetchfood = new FetchFood(this,foodSearched ,foodItemArrayList,foodAdapter);
            fetchfood.execute();
            mFoodListView.setAdapter(foodAdapter);
        }

    }
}
