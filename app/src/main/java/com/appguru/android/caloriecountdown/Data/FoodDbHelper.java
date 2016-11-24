package com.appguru.android.caloriecountdown.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jhani on 11/13/2016.
 */

public class FoodDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "food.db";

    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FOOD_TABLE = "CREATE TABLE " + FoodContract.FoodEntry.TABLE_NAME + " (" +

                // Unique keys will be auto-generated
                FoodContract.FoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FoodContract.FoodEntry.COLUMN_USER_KEY + " TEXT NOT NULL, "+
                FoodContract.FoodEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_FOOD_DESC + " TEXT NOT NULL, "+
                FoodContract.FoodEntry.COLUMN_FOOD_CALORIES + " REAL NOT NULL, "+
                FoodContract.FoodEntry.COLUMN_FOOD_PROTEIN + " REAL, "+
                FoodContract.FoodEntry.COLUMN_FOOD_FAT + " REAL, "+
                FoodContract.FoodEntry.COLUMN_FOOD_CARBS + " REAL,"+
                FoodContract.FoodEntry.COLUMN_QUANTITY + " REAL NOT NULL "+

                " );";

        final String SQL_CREATE_PROFILE_TABLE = "CREATE TABLE " + FoodContract.ProfileList.TABLE_NAME + " (" +

                // Unique keys will be auto-generated in either case.
                FoodContract.ProfileList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FoodContract.ProfileList.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, "+
                FoodContract.ProfileList.COLUMN_USER_GENDER + " TEXT NOT NULL," +
                FoodContract.ProfileList.COLUMN_USER_HEIGHT + " REAL NOT NULL," +
                FoodContract.ProfileList.COLUMN_USER_WEIGHT + " REAL NOT NULL," +
                FoodContract.ProfileList.COLUMN_USER_AGE + " INTEGER NOT NULL," +
                FoodContract.ProfileList.COLUMN_USER_GOAL + " TEXT NOT NULL," +
                FoodContract.ProfileList.COLUMN_USER_QUESTION + " TEXT NOT NULL," +
                FoodContract.ProfileList.COLUMN_USER_ANSWER + " TEXT NOT NULL," +
                FoodContract.ProfileList.COLUMN_HAS_PASSWORD + " TEXT NOT NULL," +
                FoodContract.ProfileList.COLUMN_USER_PASS + " TEXT " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_PROFILE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FOOD_TABLE);
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodContract.ProfileList.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodContract.FoodEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
