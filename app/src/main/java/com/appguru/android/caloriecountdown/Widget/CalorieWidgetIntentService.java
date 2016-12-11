package com.appguru.android.caloriecountdown.Widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.appguru.android.caloriecountdown.Data.FoodContract;
import com.appguru.android.caloriecountdown.LoginActivity;
import com.appguru.android.caloriecountdown.R;
import com.appguru.android.caloriecountdown.Utility.Utilities;

/**
 * Created by jhani on 12/8/2016.
 */

public class CalorieWidgetIntentService extends IntentService {
    private static final String[] PROFILE_COLUMNS = {
            FoodContract.FoodEntry.COLUMN_FOOD_CALORIES

    };
    // these indices must match the projection
    private static final int INDEX_CALORIE_ID = 0;
    private String description = "Calories Consumed";
    private float total_consumed;
    int layoutId;


    public CalorieWidgetIntentService() {
        super("TodayWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                MyWidgetProvider.class));
        int appWidgetId = appWidgetIds[0];

        // Get today's data from the ContentProvider
        String username = Utilities.getPreferredLocation(this);
        String todaysDate = Utilities.getTodaysDate(this);


        Uri TotalCalorieUri = FoodContract.FoodEntry.buildFoodUriWithUserIdDate(username, todaysDate);

        Cursor data = getContentResolver().query(TotalCalorieUri, PROFILE_COLUMNS, null,
                null, null);
        int j = data.getCount();
        if (j == 0) {
            layoutId = R.layout.widget_calorie;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);
            views.setImageViewResource(R.id.widget_icon, R.drawable.welcome);
            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, description);
            }
            views.setTextViewText(R.id.widget_description, description);
            views.setTextViewText(R.id.widget_value, "0");

        } else {
            while (data.moveToNext()) {
                total_consumed = total_consumed + (data.getFloat(data.getColumnIndex(FoodContract.FoodEntry.COLUMN_FOOD_CALORIES)));

            }
            data.close();


            layoutId = R.layout.widget_calorie;

            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.widget_icon, R.drawable.welcome);
            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, description);
            }
            views.setTextViewText(R.id.widget_description, description);
            views.setTextViewText(R.id.widget_value, String.valueOf((Math.round(total_consumed))));


            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, LoginActivity.class);
            intent.setAction("CalorieWidgetIntentService");
            launchIntent.putExtra("Source", "fromWidget");
            launchIntent.putExtra("username", username);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget_icon, description);
    }
}
