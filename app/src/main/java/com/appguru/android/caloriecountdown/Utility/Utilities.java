package com.appguru.android.caloriecountdown.Utility;

/**
 * Created by jhani on 11/23/2016.
 */

public class Utilities {
    private float tot_calories;


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







}
