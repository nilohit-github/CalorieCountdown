package com.appguru.android.caloriecountdown;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DetailFoodActivity extends AppCompatActivity {

    private String username;
    private String foodSearched;
    FoodAdapter foodAdapter;
    private ListView mFoodListView = null;
    public ArrayList<FoodItem> foodItemArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intent intent = getIntent();
        username = intent.getStringExtra("username");
        foodSearched = intent.getStringExtra("fooditem");
        foodItemArrayList = new ArrayList<FoodItem>();
        mFoodListView = (ListView) findViewById(R.id.listview_food);
        foodAdapter = new FoodAdapter(getApplicationContext(), R.layout.activity_detail_food, foodItemArrayList);


        if (username != null && foodSearched != null) {
            FetchFood fetchfood = new FetchFood(this, foodSearched, foodItemArrayList, foodAdapter, username);
            fetchfood.execute();
            mFoodListView.setAdapter(foodAdapter);
        }

        mFoodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodAdapter.getItem(position);
                FoodItem foodItem = foodAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), AddFoodActivity.class);
                intent.putExtra("getFood", (Parcelable) foodItem);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
