package com.appguru.android.caloriecountdown.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jhani on 11/23/2016.
 */

public class Utilities {
    private float tot_calories;
    public static final String MyPREFERENCES = "MyPrefs" ;


    public float convertHeightToMeter(int feet,int inch){

        float meter = (float) (feet*30.48);
        if(inch!=0)
        {
            meter=meter+ (float) ((float)inch*2.54);
        }

        return meter;


    }

    public float getCaloriesRequired(float weight, String userGoal){


        switch(userGoal) {
            case "MaintainWeight":
                return tot_calories = (float) ((weight*2.20462)*14);
            case "LoseWeight":
                return tot_calories = (float) (((weight*2.20462)*14)-500);
            case "GainWeight":
                return tot_calories = (float) (((weight*2.20462)*14)+500);


        }

        return tot_calories;


    }

    public int convertHeightToFeet(float height){

        int height_in_feet = (int)(height * 0.0328084);

        return height_in_feet;


    }

    public static String getPreferredLocation(Context context) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences shared = context.getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return shared.getString("nameKey","");

    }

    public static String getTodaysDate(Context context)
    {

        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }







}
