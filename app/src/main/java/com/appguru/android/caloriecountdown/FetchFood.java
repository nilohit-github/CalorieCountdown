package com.appguru.android.caloriecountdown;

import android.content.Context;
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
    final String RESULTS = "results";

    private final String LOG_TAG = FetchFood.class.getSimpleName();

    private final Context mContext;
    Toast toast;
    CharSequence text;

    public FetchFood(Context context, String foodSearched,ArrayList foodItemArrayList,FoodAdapter foodAdapter) {
        mContext = context;
        mfoodSearched =foodSearched;
        this.foodItemArrayList = foodItemArrayList;
        this.foodAdapter= foodAdapter;

    }



    private boolean DEBUG = true;

    @Override
    protected ArrayList<FoodItem> doInBackground(Void... ArrayList) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        String reviewJsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        if (mfoodSearched == null) {
            return null;
        }
        String food_searched = mfoodSearched;

        try {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            String apiKey = BuildConfig.POPULAR_MOVIES_API_KEY;
            final String REVIEWS = "reviews";
            String reviewJasonStr = null;


            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(food_searched)
                    .appendPath(REVIEWS)
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.i(LOG_TAG, "URL: " + url);


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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

            reviewJsonStr =buffer.toString();


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

        Log.d(LOG_TAG, "Movie Review Data fetched");
        try {
            return getReviewDataFromJson(reviewJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<FoodItem> getReviewDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String MOVIE_ID = "id";

        final String REVIEW_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";
        foodItemArrayList.clear();

        try {
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            String movie_id = moviesJson.getString(MOVIE_ID);

            JSONArray reviewArray = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject movieJSONObject = reviewArray.getJSONObject(i);
                String author;
                String content;

                // Get the JSON object representing the review


                author = movieJSONObject.getString("author");
                content = movieJSONObject.getString("content");

                FoodItem foodItem = new FoodItem();


                foodItemArrayList.add(foodItem);
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();

        }
        return foodItemArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<FoodItem> movieReviewArrayList) {

        if(movieReviewArrayList.size()==0)
        {
            int duration = Toast.LENGTH_LONG;
            text ="Unable to fetch movie reviews,Sorry for the inconvinience";
            toast = Toast.makeText(mContext, text, duration);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
        }

        super.onPostExecute(movieReviewArrayList);
        foodAdapter.notifyDataSetChanged();



    }
}
