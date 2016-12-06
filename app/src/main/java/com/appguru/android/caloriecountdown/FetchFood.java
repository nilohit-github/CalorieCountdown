package com.appguru.android.caloriecountdown;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jhani on 11/26/2016.
 */

public class FetchFood extends AsyncTask<Void, Void, ArrayList<FoodItem>> {

    private String mfoodSearched;
    public ArrayList<FoodItem> foodItemArrayList ;
    public FoodAdapter foodAdapter;
    final String HITS = "hits";
    private String mUsername;

    private final String LOG_TAG = FetchFood.class.getSimpleName();

    private final Context mContext;
    Toast toast;
    CharSequence text;

    public FetchFood(Context context, String foodSearched,ArrayList foodItemArrayList,FoodAdapter foodAdapter ,String username) {
        mContext = context;
        mfoodSearched =foodSearched;
        this.foodItemArrayList = foodItemArrayList;
        this.foodAdapter= foodAdapter;
        this.mUsername = username;

    }



    private boolean DEBUG = true;

    @Override
    protected ArrayList<FoodItem> doInBackground(Void... ArrayList) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        String foodJsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        if (mfoodSearched == null) {
            return null;
        }
        String food_searched = mfoodSearched;

        try {
            final String FOOD_BASE_URL = "https://api.nutritionix.com/v1_1/search/";
            final String API_KEY_PARAM = "appKey";
            final String API_ID_PARAM = "appId";
            final String results = "results";
            final String resultValue = "0:20";
            final String fields = "fields";
            final String fieldsValue = "item_name,brand_name,item_id,nf_calories,nf_total_carbohydrate,nf_sugars,nf_protein,nf_total_fat";

            String apiKey = BuildConfig.FOOD_API_KEY;
            String apiId = BuildConfig.FOOD_API_ID;
            String reviewJasonStr = null;


            Uri builtUri = Uri.parse(FOOD_BASE_URL).buildUpon()
                    .appendPath(food_searched)
                    .appendQueryParameter(results, resultValue)
                    .appendQueryParameter(fields ,fieldsValue)
                    .appendQueryParameter(API_ID_PARAM, apiId)
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.v("Url...", "URL: " + url);

            // Read the input stream into a String
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
            } catch (Exception e) {
            }
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                reviewJasonStr = null;

            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.

                reviewJasonStr = null;
            }

            foodJsonStr =buffer.toString();


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error " + e.getMessage(), e);
            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        Log.d(LOG_TAG, "Food  Data fetched");
        try {
            return getReviewDataFromJson(foodJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<FoodItem> getReviewDataFromJson(String foodJsonStr)
            throws JSONException {

        final String food_item = "id";

        final String total_calories = "id";
        final String total_carbs = "author";
        final String total_protein = "content";
        final String total_fat = "url";
        foodItemArrayList.clear();

        try {
            JSONObject moviesJson = new JSONObject(foodJsonStr);
            //String movie_id = moviesJson.getString(MOVIE_ID);

            JSONArray foodArray = moviesJson.getJSONArray(HITS);
            ArrayList<String> foodObject = new ArrayList<>();
            for (int i = 0; i < foodArray.length(); i++) {


                JSONObject foodJSONObject = foodArray.getJSONObject(i);
                String food_name;
                String food_cal;
                String food_fat;
                String food_carb;
                String food_protein;
                JSONObject finalJSONObject = foodJSONObject.getJSONObject("fields");

                // Get the JSON object representing the review


                food_name = finalJSONObject.getString("item_name");
                Log.v("Url...", "nf_item_name " + food_name);
                if(foodObject.contains(food_name))
                {
                    continue;

                }
                food_cal = finalJSONObject.getString("nf_calories");
                food_fat = finalJSONObject.getString("nf_total_fat");
                food_carb = finalJSONObject.getString("nf_total_carbohydrate");
                food_protein = finalJSONObject.getString("nf_protein");
                //Log.v("Url...", "nf_item_name " + food_name);
                Log.v("Url...", "nf_calories: " + food_cal);
                Log.v("Url...", "nf_total_fat: " + food_fat);
                Log.v("Url...", "nf_total_carbohydrate: " + food_carb);
                Log.v("Url...", "nf_protein: " + food_protein);

                FoodItem foodItem = new FoodItem();
                foodItem.setFoodCalories(food_cal);
                foodItem.setFoodName(food_name);
                foodItem.setFoodCarbs(food_carb);
                foodItem.setFoodFat(food_fat);
                foodItem.setFoodProtein(food_protein);

                foodObject.add(food_name);
                foodItemArrayList.add(foodItem);
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();

        }

        return foodItemArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<FoodItem> foodArrayList) {

        if(foodArrayList.size()==0)
        {
            int duration = Toast.LENGTH_LONG;
            text ="Unable to fetch food items,Sorry for the inconvinience";
            toast = Toast.makeText(mContext, text, duration);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("Source", "fromJava");
            intent.putExtra("username",mUsername);
            mContext.startActivity(intent);


        }

        super.onPostExecute(foodArrayList);

        foodAdapter.notifyDataSetChanged();



    }
}
