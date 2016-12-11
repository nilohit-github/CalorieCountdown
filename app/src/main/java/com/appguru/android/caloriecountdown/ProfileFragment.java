package com.appguru.android.caloriecountdown;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.appguru.android.caloriecountdown.Data.FoodContract;
import com.appguru.android.caloriecountdown.Utility.Utilities;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;
    private static final int PROFILE_LOADER = 0;
    private EditText ediTextWeight;
    private EditText editTextAnswer;
    private EditText editTextAge;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private RadioGroup radioGoalGroup;
    private RadioButton radioGoalButton;
    private Integer Feet;
    private Integer Inch;
    private NumberPicker np10;
    private NumberPicker np11;
    private Button btnDisplay;
    private boolean cancel = false;
    private float mHeight;
    private float mWeight;
    private Integer mAge;
    private View focusView = null;
    private View rootview;
    private int height_in_feet;
    private int height_in_inch;
    private int mRowsUpdated;


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        np10 = (NumberPicker) rootview.findViewById(R.id.npFeet);
        np11 = (NumberPicker) rootview.findViewById(R.id.npInch);

        np10.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np10.setMaxValue(10);

        np11.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np11.setMaxValue(11);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        np10.setWrapSelectorWheel(true);
        np11.setWrapSelectorWheel(true);

        np10.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                Feet = newVal;
            }
        });
        np11.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                Inch = newVal;
            }
        });


        Loader<Object> loader = getLoaderManager().getLoader(PROFILE_LOADER);

        if (loader != null)

        {
            getLoaderManager().destroyLoader(PROFILE_LOADER);
            getLoaderManager().initLoader(PROFILE_LOADER, null, this);
        }
        getLoaderManager().restartLoader(PROFILE_LOADER, null, this);

        addListenerOnButton();


        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        Uri ProfileUri = FoodContract.ProfileList.buildProfileIDURI(username);
        return new CursorLoader(getActivity(),
                ProfileUri,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int j = cursor.getCount();
        if (j == 0) {

            Toast.makeText(getContext(), "Unable to fetch Profile", Toast.LENGTH_SHORT)
                    .show();
        } else {
            cursor.moveToFirst();
            String user_id = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ID)));
            String has_password = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_HAS_PASSWORD)));
            String ans = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ANSWER)));
            String goal = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_GOAL)));
            String gender = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_GENDER)));
            int age = (cursor.getInt(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_AGE)));
            float height = (cursor.getFloat(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_HEIGHT)));
            float weight = (cursor.getFloat(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_WEIGHT)));
            String ques = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_QUESTION)));
            int weight_final = Math.round(weight);

            ediTextWeight = (EditText) getActivity().findViewById(R.id.editTextWt);
            editTextAge = (EditText) getActivity().findViewById(R.id.editTextAge);
            //editTextAnswer = (EditText) getActivity().findViewById(R.id.editTextAns);
            ediTextWeight.setText(String.valueOf(weight_final));
            //editTextAnswer.setText(ans);
            editTextAge.setText(String.valueOf(age));
            radioSexGroup = (RadioGroup) getActivity().findViewById(R.id.rSex);
            radioGoalGroup = (RadioGroup) getActivity().findViewById(R.id.rGoal);
            if (gender.equalsIgnoreCase("Male")) {
                radioSexGroup.check(R.id.radioMale);
            } else
                radioSexGroup.check(R.id.radioFemale);

            if (goal.equalsIgnoreCase("MaintainWeight")) {
                radioGoalGroup.check(R.id.rMaintainWeight);
            } else if (goal.equalsIgnoreCase("LoseWeight"))
                radioGoalGroup.check(R.id.rLoseWeight);
            else
                radioGoalGroup.check(R.id.rGainWeight);

            Utilities utilities = new Utilities();

            height_in_feet = utilities.convertHeightToFeet(height);
            height_in_inch = (int) Math.round((height - (height_in_feet * 30.48)) * 0.39370079);
            np10.setValue(height_in_feet);
            np11.setValue(height_in_inch);


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void addListenerOnButton() {


        radioSexGroup = (RadioGroup) getActivity().findViewById(R.id.rSex);
        radioGoalGroup = (RadioGroup) getActivity().findViewById(R.id.rGoal);

        btnDisplay = (Button) rootview.findViewById(R.id.update_profile);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                int selectedId2 = radioGoalGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioSexButton = (RadioButton) getActivity().findViewById(selectedId);
                radioGoalButton = (RadioButton) getActivity().findViewById(selectedId2);
                ediTextWeight = (EditText) getActivity().findViewById(R.id.editTextWt);
                editTextAge = (EditText) getActivity().findViewById(R.id.editTextAge);

                cancel = performValidation();


                if (!cancel) {

                    Utilities utilities = new Utilities();
                    if (Inch == null) {
                        Inch = height_in_inch;
                    }
                    if (Feet == null) {
                        Feet = height_in_feet;
                    }
                    mHeight = utilities.convertHeightToMeter(Feet, Inch);
                    mAge = Integer.parseInt(editTextAge.getText().toString());
                    mWeight = Float.parseFloat(ediTextWeight.getText().toString());

                    ContentValues values = new ContentValues();

                    values.put(FoodContract.ProfileList.COLUMN_USER_GENDER, radioSexButton.getText().toString());
                    values.put(FoodContract.ProfileList.COLUMN_USER_HEIGHT, mHeight);
                    values.put(FoodContract.ProfileList.COLUMN_USER_WEIGHT, mWeight);
                    values.put(FoodContract.ProfileList.COLUMN_USER_AGE, mAge);
                    values.put(FoodContract.ProfileList.COLUMN_USER_GOAL, radioGoalButton.getText().toString());


                    Uri updateUri = FoodContract.ProfileList.buildProfileIDURI(username);
                    mRowsUpdated = getContext().getContentResolver().update(
                            updateUri,
                            values,
                            FoodContract.ProfileList.COLUMN_USER_ID + " = ?",
                            new String[]{username});


                    //  Log.v("inserted uri", "value::" + insertedUri.toString());
                    Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("goal", radioGoalButton.getText().toString());
                    intent.putExtra("weight", mWeight);
                    intent.putExtra("Source", "fromProfile");

                    startActivity(intent);


                }
            }
        });


    }

    private boolean performValidation() {

        cancel = false;
        if ((ediTextWeight.getText().toString()).matches("")) {
            ediTextWeight.setError("weight is empty");
            focusView = ediTextWeight;
            cancel = true;
        } else if ((editTextAge.getText().toString().matches(""))) {
            editTextAge.setError("Age is empty");
            focusView = editTextAge;
            cancel = true;
        } else
            return false;


        return cancel;
    }
}
