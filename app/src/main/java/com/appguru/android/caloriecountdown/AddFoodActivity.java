package com.appguru.android.caloriecountdown;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.appguru.android.caloriecountdown.Data.FoodContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFoodActivity extends AppCompatActivity {

    String username;
    String foodName;
    float Totalcal;
    float TotalFat;
    float TotalCarbs;
    float TotalProtein;
    TextView textView;
    Integer count;
    private Button btnDisplay;
    //foodSearched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NumberPicker np1 = (NumberPicker) findViewById(R.id.np10);

        np1.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np1.setMaxValue(10);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        np1.setWrapSelectorWheel(true);


        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                count= newVal;

            }
        });
        Intent intent = getIntent();
        username= intent.getStringExtra("username");
        FoodItem foodItem = (FoodItem) intent.getParcelableExtra("getFood");

        textView = (TextView) findViewById(R.id.selected_food);
        textView.setText(foodItem.getFoodName());
        foodName = foodItem.getFoodName().toString();

        textView = (TextView) findViewById(R.id.tot_cal);
        textView.setText("Total Calories : "+foodItem.getFoodCalories());
        Totalcal= Float.parseFloat(foodItem.getFoodCalories());

        textView = (TextView) findViewById(R.id.tot_carbs);
        textView.setText("Total Carbohydrates : "+foodItem.getFoodCarbs());
        TotalCarbs=Float.parseFloat(foodItem.getFoodCarbs());

        textView = (TextView) findViewById(R.id.tot_fat);
        textView.setText("Total Fat : "+foodItem.getFoodFat());
        TotalFat=Float.parseFloat(foodItem.getFoodFat());

        textView = (TextView) findViewById(R.id.tot_protein);
        textView.setText( "Total Protein : "+foodItem.getFoodProtein());
        TotalProtein=Float.parseFloat(foodItem.getFoodProtein());
        Time dayTime = new Time();
        dayTime.setToNow();
        System.currentTimeMillis();

        addListenerOnButton();


    }


    public void addListenerOnButton() {


        btnDisplay = (Button) findViewById(R.id.addFood);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                    if(count == null)
                    {
                        count =1;
                    }



                Log.v("count:::", String.valueOf(count));
                Calendar c = Calendar.getInstance();
                //System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());
                Log.v("inserted date", "date+::" + formattedDate);

                Totalcal = Totalcal*count;
                TotalCarbs=TotalCarbs*count;
                TotalProtein=TotalProtein*count;
                TotalFat=TotalFat*count;
                Log.v("inserted calories", "cal+::" + Totalcal);


                    ContentValues values = new ContentValues();
                    values.put(FoodContract.FoodEntry.COLUMN_USER_KEY, username);
                    values.put(FoodContract.FoodEntry.COLUMN_DATE ,formattedDate);
                    values.put(FoodContract.FoodEntry.COLUMN_FOOD_DESC,foodName);
                    values.put(FoodContract.FoodEntry.COLUMN_FOOD_CALORIES, Totalcal);
                    values.put(FoodContract.FoodEntry.COLUMN_FOOD_PROTEIN, TotalProtein);
                    values.put(FoodContract.FoodEntry.COLUMN_FOOD_FAT,TotalFat);
                    values.put(FoodContract.FoodEntry.COLUMN_FOOD_CARBS, TotalCarbs);
                    values.put(FoodContract.FoodEntry.COLUMN_QUANTITY, count);


                    Uri insertedUri = getApplicationContext().getContentResolver().insert(FoodContract.FoodEntry.CONTENT_URI, values);
                    Log.v("inserted uri", "value::" + insertedUri.toString());
                    Toast.makeText(AddFoodActivity.this, "Food added to Database", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("Source", "fromAddFoodActivity");
                    startActivity(intent);


                }
            }
        );


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
