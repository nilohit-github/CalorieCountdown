package com.appguru.android.caloriecountdown;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jhani on 11/26/2016.
 */

public class FoodAdapter extends ArrayAdapter<FoodItem> {

    private Context context;
    int layoutResourceId;
    ArrayList<FoodItem> foodItemArrayList;
    int listOfFood = 0;
    private LayoutInflater inflater;


    public FoodAdapter(Context context, int resource, ArrayList<FoodItem> foodItemArrayList) {
        super(context, resource, foodItemArrayList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.foodItemArrayList = foodItemArrayList;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {


        if (foodItemArrayList != null) {
            listOfFood = foodItemArrayList.size();
        }

        return listOfFood;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
           convertView = inflater.inflate(R.layout.food_list, parent, false);
            Log.v("food adapter", "food adapter:::::: " +foodItemArrayList.get(position).getFoodName() );
           ((TextView) convertView.findViewById(R.id.reviewer_textview)).setText(foodItemArrayList.get(position).getFoodName());
           // ((TextView) convertView.findViewById(R.id.content_textview)).setText(movieReviewArrayList.get(position).getCONTENT());

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

}
