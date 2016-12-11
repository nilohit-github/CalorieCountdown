package com.appguru.android.caloriecountdown;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appguru.android.caloriecountdown.Data.FoodContract;

public class ForgotActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String username;
    private static final int FORGOT_LOADER = 0;
    private String contains_password = "N";
    private TextView textViewQ;
    private LinearLayout container;
    private Button btnDisplay;
    private Button btnSave;
    private String answer;
    private boolean cancel = false;
    private View focusView = null;
    private EditText mEditAnswer;
    private EditText mPassNew;
    private String newPassword;
    private String dbAns;
    private boolean passed;
    private int mRowsUpdated;
    private boolean btn1Clicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        Loader<Object> loader = getSupportLoaderManager().getLoader(FORGOT_LOADER);

        if (loader != null) {
            getSupportLoaderManager().destroyLoader(FORGOT_LOADER);
            getSupportLoaderManager().initLoader(FORGOT_LOADER, null, this);
        }
        getSupportLoaderManager().restartLoader(FORGOT_LOADER, null, this);
        // addListenerOnButton();

        //add button logic here not working through a seperate function

        btnDisplay = (Button) findViewById(R.id.security_ans_submit_button);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                mEditAnswer = (EditText) findViewById(R.id.SecurityAnswer);


                cancel = performValidation();


                if (!cancel) {
                    btnDisplay.setEnabled(false);
                    answer = mEditAnswer.getText().toString();
                    container = (LinearLayout) findViewById(R.id.container);
                    LayoutInflater layoutInflate = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = layoutInflate.inflate(R.layout.reset_password, null);
                    container.addView(view);
                    Button clickButton = (Button) findViewById(R.id.save_button);
                    clickButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mPassNew = (EditText) findViewById(R.id.password_new);


                            passed = performPassValidation();


                            if (passed) {
                                //btnSave.setEnabled(false);
                                newPassword = mPassNew.getText().toString();
                                ContentValues values = new ContentValues();
                                values.put(FoodContract.ProfileList.COLUMN_USER_PASS, newPassword);

                                Uri updateUri = FoodContract.ProfileList.buildProfileIDURI(username);
                                mRowsUpdated = getApplicationContext().getContentResolver().update(
                                        updateUri,
                                        values,
                                        FoodContract.ProfileList.COLUMN_USER_ID + " = ?",
                                        new String[]{username});

                                //  Log.v("inserted uri", "value::" + insertedUri.toString());
                                Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);

                            }


                        }
                    });
                }
            }


        });

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
        textViewQ = (TextView) findViewById(R.id.SecurityQuestion);
        if (j == 0) {
            Toast.makeText(ForgotActivity.this, "Unable to find account.", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            cursor.moveToFirst();
            contains_password = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_HAS_PASSWORD)));
            if (contains_password.equalsIgnoreCase("N")) {
                Toast.makeText(ForgotActivity.this, "No password associated with this username.", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            if (contains_password.equalsIgnoreCase("Y")) {
                dbAns = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ANSWER)));
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


    public void addListenerOnButton() {


    }


    private boolean performValidation() {

        cancel = false;
        if ((mEditAnswer.getText().toString()).matches("")) {
            mEditAnswer.setError("answer is empty");
            focusView = mEditAnswer;
            cancel = true;
        } else if (!(mEditAnswer.getText().toString().equalsIgnoreCase(dbAns))) {
            mEditAnswer.setError("Sorry,Your answer does not match.");
            focusView = mEditAnswer;
            cancel = true;
        } else
            return false;


        return cancel;
    }

    private boolean performPassValidation() {
        passed = false;

        if (((mPassNew.getText().toString()).length()) > 4) {
            passed = true;
        } else {

            mPassNew.setError("password is too short");
            focusView = mPassNew;
        }
        return passed;


    }

}
