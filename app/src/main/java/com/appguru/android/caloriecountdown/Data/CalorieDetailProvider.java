package com.appguru.android.caloriecountdown.Data;
/**
 * Created by jhani on 11/13/2016.
 */


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Defines table and column names for the weather database.
 */
public class CalorieDetailProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodDbHelper mOpenHelper;
    static final int FOOD = 100;
    static final int FOOD_WITH_USER_ID = 101;
    static final int FOOD_WITH_USER_AND_DATE = 102;
    static final int PROFILE = 300;
    static final int PROFILE_WITH_USER_ID = 301;

    private static final String sFoodByID =
            FoodContract.FoodEntry.TABLE_NAME+
                    "." + FoodContract.FoodEntry.COLUMN_USER_KEY + " = ? ";
    private static final String sFoodByIDAndDate =
            FoodContract.FoodEntry.TABLE_NAME+
                    "." + FoodContract.FoodEntry.COLUMN_USER_KEY + " = ? AND " +
    FoodContract.FoodEntry.COLUMN_DATE + " = ? ";
    private static final String sProfileByID =
            FoodContract.ProfileList.TABLE_NAME+
                    "." + FoodContract.ProfileList.COLUMN_USER_ID+ " = ? ";







    static UriMatcher buildUriMatcher() {

        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FoodContract.CONTENT_AUTHORITY;




        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.


        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, FoodContract.PATH_FOOD, FOOD); // JUST FOR PLAIN VANILLA INSERT query
        matcher.addURI(authority, FoodContract.PATH_FOOD + "/*", FOOD_WITH_USER_ID);// FOR FOOD QUERY WITH USER ID FOR BUILDING MP CHART
        matcher.addURI(authority, FoodContract.PATH_FOOD + "/*/*", FOOD_WITH_USER_AND_DATE);// QUERY FOR DAILY FOOD CALORIE DETAILS
        matcher.addURI(authority, FoodContract.PATH_PROFILE, PROFILE);//JUST FOR PLAIN VANILLA INSERT query
        matcher.addURI(authority, FoodContract.PATH_PROFILE + "/*", PROFILE_WITH_USER_ID);// TO CHECK IF THE USER ALREADY EXIST



        return matcher;

    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(FoodContract.FoodEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(FoodContract.FoodEntry.COLUMN_DATE);
            values.put(FoodContract.FoodEntry.COLUMN_DATE,FoodContract.normalizeDate(dateValue));

        }
    }



    @Override
    public boolean onCreate() {
        mOpenHelper = new FoodDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                               String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        String user_id = FoodContract.FoodEntry.getUSERIDFromUri(uri);
        Log.v(":::in query before sw ","query"+user_id);
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case FOOD_WITH_USER_AND_DATE:
            {
                user_id = FoodContract.FoodEntry.getUSERIDFromUri(uri);
                //Long date_field = FoodContract.FoodEntry.getDateFromUri(uri);
                String date_field  = FoodContract.FoodEntry.getDateFromUri(uri);
                Log.v("inside user id","query"+user_id);
                Log.v("inside date_field","query"+date_field);
                selection = sFoodByIDAndDate;
                selectionArgs = new String[]{user_id, date_field};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.FoodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "weather/*"
            case FOOD_WITH_USER_ID: {
                user_id = FoodContract.FoodEntry.getUSERIDFromUri(uri);
                Log.v("inside food with id ","query"+user_id);
                selection = sFoodByID;
                selectionArgs = new String[]{user_id};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.FoodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "weather"
            case PROFILE_WITH_USER_ID: {
                user_id = FoodContract.ProfileList.getProfileIDFromUri(uri);
                Log.v(":::in profile with id ","query"+user_id);
                selection = sProfileByID;
                selectionArgs = new String[]{user_id};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.ProfileList.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        String test = "test";
        Log.v(":::in profile return ",":::"+test);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case PROFILE_WITH_USER_ID:
                return  FoodContract.ProfileList.CONTENT_ITEM_TYPE;
            case FOOD_WITH_USER_ID:
                return FoodContract.FoodEntry.CONTENT_TYPE;
            case FOOD_WITH_USER_AND_DATE:
                return FoodContract.FoodEntry.CONTENT_TYPE;
            case FOOD:
                return FoodContract.FoodEntry.CONTENT_TYPE;
            case PROFILE:
                return FoodContract.ProfileList.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FOOD: {
              //  normalizeDate(values);
                long _id = db.insert(FoodContract.FoodEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodContract.FoodEntry.buildFoodUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PROFILE: {

                long _id = db.insert(FoodContract.ProfileList.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodContract.ProfileList.buildProfileUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PROFILE_WITH_USER_ID:

                rowsUpdated = db.update(FoodContract.ProfileList.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
