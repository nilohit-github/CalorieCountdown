package com.appguru.android.caloriecountdown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appguru.android.caloriecountdown.Data.FoodContract;
import com.appguru.android.caloriecountdown.Utility.Utilities;
import com.google.android.gms.plus.PlusOneButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link AddFoodFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int CALORIE_LOADER = 0;
    private PlusOneButton mPlusOneButton;
    private Button btnDisplay;
    private Boolean internetAvailable;
    private EditText mEditFood;
    private String searchedFood;
    private String username;
    private String goal;
    private float weight;
    private float tot_calories;
    private CharSequence text = "No network coverage)";
    private Toast toast;
    private View rootView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private OnFragmentInteractionListener mListener;
    private float tot_calories_consumed;
    private float tot_calories_remaining;
    private String activity;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String WeightKey = "weightKey";
    public static final String GoalKey = "goalKey";


    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Log.v("query date", "date+::" + formattedDate);

        Uri CaloriSearchUri = FoodContract.FoodEntry.buildFoodUriWithUserIdDate(username,formattedDate);

        Log.v("login activity", "inside on create load "+CaloriSearchUri.toString() );
        return new CursorLoader(getActivity(),
                CaloriSearchUri,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {



        textView1 = (TextView)getActivity().findViewById(R.id.CalorieConsumed);
        textView2 = (TextView)getActivity().findViewById(R.id.CaloriesRequired);
        textView3 = (TextView)getActivity().findViewById(R.id.CalorieRemaining);
        textView2.setText("Your total calorie requirement : "+Math.round(tot_calories));

        int j = cursor.getCount();
        if(j==0)
        {
            Log.v("No calories consumed ", ":::15 "+"zero call" );

            textView1.setText("Total Calories consumed : "+0);
            textView3.setText("Total Calories remaining for today : "+Math.round(tot_calories));

        }
        else{

            while (cursor.moveToNext()) {
               tot_calories_consumed = tot_calories_consumed+(cursor.getFloat(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_FOOD_CALORIES)));
                Log.v("calories consumed ", ":::15 "+tot_calories);
                tot_calories_remaining = tot_calories - tot_calories_consumed;
            }
            textView1.setText("Total Calories consumed : "+Math.round(tot_calories_consumed));
            textView3.setText("Total Calories remaining for today : "+Math.round(tot_calories_remaining));

            cursor.close();

           // Log.v("calories consumed ", ":::15 "+tot_calories);

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

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onSearchClick(String username,String foodItem);
    }

    public AddFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFoodFragment newInstance(String param1, String param2) {
        AddFoodFragment fragment = new AddFoodFragment();
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
        rootView = inflater.inflate(R.layout.fragment_add_food, container, false);
        Intent intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        activity = intent.getStringExtra("Source");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(activity.equalsIgnoreCase("fromLogin"))
        {
            goal = intent.getStringExtra("goal");
            weight = intent.getFloatExtra("weight",1);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Name, username);
            editor.putFloat(WeightKey, weight);
            editor.putString(GoalKey, goal);
            editor.commit();
        }

        Utilities utilities = new Utilities();
        tot_calories =utilities.getCaloriesRequired(sharedpreferences.getFloat(WeightKey,0),sharedpreferences.getString(GoalKey,""));
        Log.v("fragment food", "tCal:::::: " +tot_calories );
        Log.v("fragment food", "tgoal:::::: " +sharedpreferences.getString(GoalKey,"") );

        Loader<Object> loader = getLoaderManager().getLoader(CALORIE_LOADER);

        if (loader != null)

        {
            getLoaderManager().destroyLoader(CALORIE_LOADER);
            getLoaderManager().initLoader(CALORIE_LOADER, null, this);
        }
        getLoaderManager().restartLoader(CALORIE_LOADER, null, this);


        addListenerOnButton();

        //Find the +1 button
       // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
//        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }


    public void addListenerOnButton() {



        btnDisplay = (Button)rootView.findViewById(R.id.search_food_button);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

             internetAvailable =   isNetworkAvailable();

                if(internetAvailable)
                {
                    mEditFood = (EditText)rootView.findViewById(R.id.editTextFood);
                    String searchedFood = mEditFood.getText().toString();
                    Log.v("search clicked", "name:::::: " +username );
                    Log.v("search clicked", "food:::::: " +searchedFood );
                    ((Callback) getActivity()).onSearchClick(username,searchedFood);
                }
                else
                {
                    int duration = Toast.LENGTH_LONG;
                    toast = Toast.makeText(getContext(), text, duration);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });


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

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }



}
