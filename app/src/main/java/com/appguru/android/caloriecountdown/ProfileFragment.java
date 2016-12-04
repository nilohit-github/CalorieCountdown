package com.appguru.android.caloriecountdown;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.appguru.android.caloriecountdown.Data.FoodContract;


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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        Log.v("fragment profile", "user:::::: " +username );


        Loader<Object> loader = getLoaderManager().getLoader(PROFILE_LOADER);

        if (loader != null)

        {
            getLoaderManager().destroyLoader(PROFILE_LOADER);
            getLoaderManager().initLoader(PROFILE_LOADER, null, this);
        }
        getLoaderManager().restartLoader(PROFILE_LOADER, null, this);


        return view;
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
        Log.v("login activity", "inside on create load email:: "+username );
        Log.v("login activity", "inside on create load "+ProfileUri.toString() );
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
        if(j==0)
        {

            Toast.makeText(getContext(), "Unable to fetch Profile", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            cursor.moveToFirst();
            Log.v("show profile ", "profile:: " + username);


            String user_id = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ID)));
            Log.v("login activity", "cursor values:: email returned " + user_id);
            String has_password = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_HAS_PASSWORD)));
            Log.v("login activity", "cursor values:: has password " + has_password);
            String ans = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_ANSWER)));
            String goal = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_GOAL)));
            String gender = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_GENDER)));
            int age = (cursor.getInt(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_AGE)));
            float height = (cursor.getFloat(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_HEIGHT)));
            float weight = (cursor.getFloat(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_WEIGHT)));
            String ques = (cursor.getString(cursor.getColumnIndex(FoodContract.ProfileList.COLUMN_USER_QUESTION)));
            int weight_final = Math.round(weight);
            Log.v("login activity", "cursor values:::: ans " + ans);
            Log.v("login activity", "cursor values:::: goal " + goal);
            Log.v("login activity", "cursor values:::: gender " + gender);
            Log.v("login activity", "cursor values:::: age " + age);
            Log.v("login activity", "cursor values:::: height " + height);
            Log.v("login activity", "cursor values:::: weight " + weight);
            Log.v("login activity", "cursor values:::: quest " + ques);
            ediTextWeight = (EditText) getActivity().findViewById(R.id.editTextWt);
            editTextAge = (EditText) getActivity().findViewById(R.id.editTextAge);
            editTextAnswer = (EditText) getActivity().findViewById(R.id.editTextAns);
            ediTextWeight.setText(String.valueOf(weight_final));
            //editTextAnswer.setText(ans);
            editTextAge.setText(String.valueOf(age));
            radioSexGroup = (RadioGroup)getActivity().findViewById(R.id.rSex);
            radioGoalGroup = (RadioGroup)getActivity().findViewById(R.id.rGoal);
            if(gender.equalsIgnoreCase("Male"))
            {
                radioSexGroup.check(R.id.radioMale);
            }
            else
                radioSexGroup.check(R.id.radioFemale);

            if(goal.equalsIgnoreCase("MaintainWeight"))
            {
                radioGoalGroup.check(R.id.rMaintainWeight);
            }
            else if(goal.equalsIgnoreCase("LoseWeight"))
                radioGoalGroup.check(R.id.rLoseWeight);
            else
                radioGoalGroup.check(R.id.rGainWeight);



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
}
