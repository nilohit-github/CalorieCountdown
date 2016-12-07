package com.appguru.android.caloriecountdown;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.appguru.android.caloriecountdown.Data.FoodContract;

public class ForgotActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private String username;
    private static final int FORGOT_LOADER = 0;
    private String contains_password ="N";
    private TextView textViewQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        username= intent.getStringExtra("username");

        Loader<Object> loader = getSupportLoaderManager().getLoader(FORGOT_LOADER);

        if (loader != null)
        {
            getSupportLoaderManager().destroyLoader(FORGOT_LOADER);
            getSupportLoaderManager().initLoader(FORGOT_LOADER, null, this);
        }
        getSupportLoaderManager().restartLoader(FORGOT_LOADER, null, this);

    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("forgot loader in ", ":::13 "+username );

        Uri AuthenticationUri = FoodContract.ProfileList.buildProfileIDURI(username);
        return new CursorLoader(this,
                AuthenticationUri,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int j = cursor.getCount();
        textViewQ = (TextView)findViewById(R.id.SecurityQuestion);
        if(j==0)
        {
            Toast.makeText(ForgotActivity.this, "Unable to find account.", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            cursor.moveToFirst();
            contains_password = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_HAS_PASSWORD)));
            Log.v("forgot activity", "cursor values:: has password " + contains_password);
            if(contains_password.equalsIgnoreCase("N"))
            {
                Toast.makeText(ForgotActivity.this, "No password associated with this username.", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            if(contains_password.equalsIgnoreCase("Y"))
            {
                String ans = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ANSWER)));
                String question = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_QUESTION)));
                textViewQ.setText(question);

            }

        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
