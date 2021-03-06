package com.appguru.android.caloriecountdown.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by jhani on 11/13/2016.
 */

public class FoodContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.appguru.android.caloriecountdown";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FOOD = "food";
    public static final String PATH_PROFILE = "userprofile";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class ProfileList implements BaseColumns {
        public static final String TABLE_NAME = "userprofile";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_PASS = "user_pass";
        public static final String COLUMN_USER_GENDER = "gender";
        public static final String COLUMN_USER_WEIGHT = "weight";
        public static final String COLUMN_USER_HEIGHT = "height";
        public static final String COLUMN_USER_GOAL = "goal";
        public static final String COLUMN_USER_QUESTION = "security_question";
        public static final String COLUMN_USER_ANSWER = "security_answer";
        public static final String COLUMN_HAS_PASSWORD = "has_password";
        public static final String COLUMN_USER_AGE = "age";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILE;

        public static Uri buildProfileUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildProfileIDURI(String user_id) {

            return CONTENT_URI.buildUpon().appendPath(user_id).build();

        }

        public static String getProfileIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }

    /* Inner class that defines the table contents of the weather table */
    public static final class FoodEntry implements BaseColumns {

        public static final String TABLE_NAME = "food";
        public static final String COLUMN_USER_KEY = "user_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_FOOD_DESC = "food_desc";
        public static final String COLUMN_FOOD_CALORIES = "calories";
        public static final String COLUMN_FOOD_PROTEIN = "protein";
        public static final String COLUMN_FOOD_CARBS = "carbs";
        public static final String COLUMN_FOOD_FAT = "fat";
        public static final String COLUMN_QUANTITY = "quantity";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOD;


        public static Uri buildFoodUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildFoodUriWithUserId(String userID) {

            return CONTENT_URI.buildUpon().appendPath(userID).build();
            //return null;
        }


        public static Uri buildFoodUriWithUserIdDate(String userID, String formattedDate) {
            return CONTENT_URI.buildUpon().appendPath(userID)
                    .appendPath(formattedDate).build();
        }

        public static Uri buildFoodUriWithUserIdDateRange(String userID, String formattedDate, String dateRange) {
            return CONTENT_URI.buildUpon().appendPath(userID)
                    .appendPath(formattedDate).appendPath(dateRange).build();
        }


        public static String getUSERIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getDateRangeFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }


    }

}
