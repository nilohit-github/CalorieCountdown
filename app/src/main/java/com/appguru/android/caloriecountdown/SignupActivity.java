package com.appguru.android.caloriecountdown;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appguru.android.caloriecountdown.Data.FoodContract;
import com.appguru.android.caloriecountdown.Utility.Utilities;

public class SignupActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Button btnDisplay;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private RadioGroup radioGoalGroup;
    private RadioButton radioGoalButton;
    private Integer Feet;
    private Integer Inch;
    private float mheight;
    private String username;
    private String haspass;
    private String password;
    private String security;
    private EditText mEditWeight;
    private EditText mEditAnswer;
    private EditText mEditAge;
    private float weight;
    private Integer age;
    private String answer;
    boolean cancel = false;
    View focusView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        NumberPicker np1 = (NumberPicker) findViewById(R.id.np1);
        NumberPicker np2 = (NumberPicker) findViewById(R.id.np2);
        Intent intent = getIntent();
         username= intent.getStringExtra("username");
         haspass = intent.getStringExtra("haspass");
         if(haspass.equalsIgnoreCase("Y"));
         {
             password = intent.getStringExtra("password");
//             Log.v("pass:::",password);
         }
        Log.v("user id:::",username);
        //Log.v("pass",password);
        Log.v("has pass:::",haspass);
        addListenerOnSpinnerItemSelection();
        addListenerOnButton();

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        np1.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np1.setMaxValue(10);

        np2.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np2.setMaxValue(11);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        np1.setWrapSelectorWheel(true);
        np2.setWrapSelectorWheel(true);

        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker

            }
        });
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker

            }
        });

        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                 Feet = newVal;
            }
        });
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                Inch = newVal;
            }
        });


    }


    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void addListenerOnButton() {

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioGoalGroup = (RadioGroup) findViewById(R.id.radioGoal);

        btnDisplay = (Button) findViewById(R.id.email_sign_in_button);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                int selectedId2 = radioGoalGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);
                radioGoalButton = (RadioButton) findViewById(selectedId2);
                mEditWeight = (EditText) findViewById(R.id.editText1);
                mEditAnswer = (EditText) findViewById(R.id.editText6);
                mEditAge = (EditText) findViewById(R.id.editText4);

               cancel = performValidation();


                if (!cancel) {

                    Utilities utilities = new Utilities();
                    if(Inch == null)
                    {
                        Inch =0;
                    }
                    mheight = utilities.convertHeightToMeter(Feet, Inch);
                    security = String.valueOf(spinner1.getSelectedItem());
                    age = Integer.parseInt(mEditAge.getText().toString());
                    weight = Float.parseFloat(mEditWeight.getText().toString());
                    answer = mEditAnswer.getText().toString();

                    Log.v("user id", username);
//                Log.v("pass",password);
                    Log.v("signup security", security);
                    Log.v("signup Feet", Feet.toString());
                    Log.v("signup Inch", Inch.toString());
                    Log.v("signup height", String.valueOf(mheight));
                    Log.v("SEX", radioSexButton.getText().toString());
                    Log.v("Goal", radioGoalButton.getText().toString());
                    Log.v("has pass", haspass);
                    Log.v("Age", age.toString());
                    Log.v("answer", answer);
                    Log.v("weight", String.valueOf(weight));



                    ContentValues values = new ContentValues();
                    values.put(FoodContract.ProfileList.COLUMN_USER_ID, username);
                    values.put(FoodContract.ProfileList.COLUMN_USER_GENDER, radioSexButton.getText().toString());
                    values.put(FoodContract.ProfileList.COLUMN_USER_HEIGHT, mheight);
                    values.put(FoodContract.ProfileList.COLUMN_USER_WEIGHT, weight);
                    values.put(FoodContract.ProfileList.COLUMN_USER_AGE, age);
                    values.put(FoodContract.ProfileList.COLUMN_USER_GOAL, radioGoalButton.getText().toString());
                    values.put(FoodContract.ProfileList.COLUMN_USER_QUESTION, security);
                    values.put(FoodContract.ProfileList.COLUMN_USER_ANSWER, answer);
                    values.put(FoodContract.ProfileList.COLUMN_HAS_PASSWORD, haspass);
                    if (haspass.equalsIgnoreCase("Y")) {
                        values.put(FoodContract.ProfileList.COLUMN_USER_PASS, password);
                    }


                    Uri insertedUri = getApplicationContext().getContentResolver().insert(FoodContract.ProfileList.CONTENT_URI, values);
                    Log.v("inserted uri", "value::" + insertedUri.toString());
                    Toast.makeText(SignupActivity.this, "Added to Favorite", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);


                }
            }
        });


    }

    private boolean performValidation() {

        cancel = false;
        if ((mEditWeight.getText().toString()).matches("")) {
            mEditWeight.setError("weight is empty");
            focusView = mEditWeight;
            cancel = true;
        }

        else if ((mEditAge.getText().toString().matches(""))) {
            mEditAge.setError("Age is empty");
            focusView = mEditAge;
            cancel = true;
        }
        else if(Feet == null)
        {
            TextView errorText = (TextView)findViewById(R.id.height);
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            //changes the selected item text to this
            //spinner1.setError("Please select a security question");
            focusView = errorText;
            cancel = true;

        }

        else if ((String.valueOf(spinner1.getSelectedItem()).matches(""))) {
            TextView errorText = (TextView)spinner1.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("my actual error text");//changes the selected item text to this
            //spinner1.setError("Please select a security question");
            focusView = spinner1;
            cancel = true;
        }

        else if (mEditAnswer.getText().toString().matches("")) {
            mEditAnswer.setError("answer is empty");
            focusView = mEditAnswer;
            cancel = true;
        }
        else
            return false;


        return cancel;
    }
}