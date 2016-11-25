package com.appguru.android.caloriecountdown;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appguru.android.caloriecountdown.Data.FoodContract;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int LOGIN_LOADER = 0;
    private String email;
    private String password;
    private String email_returned;
    private String password_returned;
    private String has_password ="N";
    private Boolean passed;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
   // private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
     /*   if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;

            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Log.v("show progress ", ":::12 "+email );
          //  mAuthTask = new UserLoginTask(email, password);
           // mAuthTask.execute((Void) null);
            Loader<Object> loader = getSupportLoaderManager().getLoader(LOGIN_LOADER);

            if (loader != null)
                Log.v("show progress ", ":::100 "+email );
            {
                getSupportLoaderManager().destroyLoader(LOGIN_LOADER);
                getSupportLoaderManager().initLoader(LOGIN_LOADER, null, this);
            }
            getSupportLoaderManager().restartLoader(LOGIN_LOADER, null, this);







        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        passed =  password.length() > 4;
        if(passed){
            has_password = "Y";
        }
        return passed;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        Log.v("show progress ", ":::1 "+email );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Log.v("show progress ", ":::2 "+email );
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            Log.v("show progress ", ":::3 "+email );
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            Log.v("show progress ", ":::4 "+email );
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.v("show progress ", ":::5 "+email );
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    Log.v("show progress ", ":::6 "+email );
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            Log.v("show progress ", ":::7 "+email );
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.v("show progress ", ":::8 "+email );
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    Log.v("show progress ", ":::9 "+email );
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            Log.v("show progress ", ":::10 "+email );
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            Log.v("show progress ", ":::11 "+email );
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.v("show progress ", ":::13 "+email );

        Uri AuthenticationUri = FoodContract.ProfileList.buildProfileIDURI(email);
        Log.v("login activity", "inside on create load email:: "+email );
        Log.v("login activity", "inside on create load "+AuthenticationUri.toString() );
        return new CursorLoader(this,
                AuthenticationUri,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.v("show progress ", ":::14 "+email );
        Log.v("login activity", "inside load finished " );
        int j = cursor.getCount();
        if(j==0)
        {
            Log.v("show progress ", ":::15 "+email );
            Intent intent = new Intent(this, SignupActivity.class);
            intent.putExtra("username", email);
            intent.putExtra("haspass", has_password);
            if(has_password.equalsIgnoreCase("Y"))
            {
                intent.putExtra("password", password);
            }
            Log.v("user id",email);
            Log.v("pass",password);
            Log.v("has pass",has_password);
            startActivity(intent);
        }
        else {
            cursor.moveToFirst();
            Log.v("show progress ", ":::16 "+email );

            email_returned =(cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ID)));
            Log.v("login activity", "cursor values:: email returned " +email_returned );
            has_password =(cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_HAS_PASSWORD)));
            Log.v("login activity", "cursor values:: has password " +has_password );
            String ans =(cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ANSWER)));
            String goal = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_GOAL)));
            String gender = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_GENDER)));
            int age =(cursor.getInt(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_AGE)));
            float height = (cursor.getFloat(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_HEIGHT)));
            float weight = (cursor.getFloat(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_WEIGHT)));
            String ques =(cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_QUESTION)));
            Log.v("login activity", "cursor values:::: ans " +ans );
            Log.v("login activity", "cursor values:::: goal " +goal );
            Log.v("login activity", "cursor values:::: gender " +gender );
            Log.v("login activity", "cursor values:::: age " +age );
            Log.v("login activity", "cursor values:::: height " +height );
            Log.v("login activity", "cursor values:::: weight " +weight );
            Log.v("login activity", "cursor values:::: quest " +ques );


            if(has_password.equalsIgnoreCase("Y")){
                Log.v("show progress ", ":::17 "+email );
                password_returned =(cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_PASS)));
                Log.v("login activity", "cursor values:::: pass returned " +password_returned );
                Log.v("show progress ", "::::18 "+email );
                if(password_returned.equalsIgnoreCase(password))
                {
                    Log.v("show progress ", "::::19 "+email );
                    cursor.close();
                    Log.v("show progress ", "::::20 "+email );
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, email);
                    startActivity(intent);
                }
                else{

                    mLoginFormView.requestFocus();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    this.recreate();





                }
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, email);
                startActivity(intent);
            }




        }
        //addEmailsToAutoComplete(emails);
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










}

