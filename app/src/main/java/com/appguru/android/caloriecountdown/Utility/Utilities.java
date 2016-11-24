package com.appguru.android.caloriecountdown.Utility;

/**
 * Created by jhani on 11/23/2016.
 */

public class Utilities {


    public float convertHeightToMeter(int feet,int inch){

        float meter = (float) (feet*30.48);
        if(inch!=0)
        {
            meter=meter+ (float) ((float)inch*2.54);
        }

        return meter;


    }



}
