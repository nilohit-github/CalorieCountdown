package com.appguru.android.caloriecountdown;

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

import com.appguru.android.caloriecountdown.Data.FoodContract;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MacronutrientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MacronutrientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MacronutrientFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;
    private float totalFatConsumed;
    private float totalProteinConsumed;
    private float totalCarbsConsumed;
    private static final int NUTRIENT_LOADER = 0;
    BarChart chart;


    private OnFragmentInteractionListener mListener;

    public MacronutrientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MacronutrientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MacronutrientFragment newInstance(String param1, String param2) {
        MacronutrientFragment fragment = new MacronutrientFragment();
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
        chart = new BarChart(getContext());
        View view = (chart);

        // Inflate the layout for this fragment
        // View view = inflater.inflate(R.layout.fragment_macronutrient, container, false);
        Intent intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        Loader<Object> loader = getLoaderManager().getLoader(NUTRIENT_LOADER);

        if (loader != null)

        {
            getLoaderManager().destroyLoader(NUTRIENT_LOADER);
            getLoaderManager().initLoader(NUTRIENT_LOADER, null, this);
        }
        getLoaderManager().restartLoader(NUTRIENT_LOADER, null, this);


        return view;

    }

    private void drawBarChart() {

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(totalCarbsConsumed, 0));
        entries.add(new BarEntry(totalFatConsumed, 1));
        entries.add(new BarEntry(totalProteinConsumed, 2));
        //entries.add(new BarEntry(12f, 3));
        BarDataSet dataset = new BarDataSet(entries, "# Macro-nutrients consumed today(grams)");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Carbohydrates");
        labels.add("Fat");
        labels.add("Protein");


        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.animateY(5000);

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
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        Uri CaloriSearchUri = FoodContract.FoodEntry.buildFoodUriWithUserIdDate(username, formattedDate);

        return new CursorLoader(getActivity(),
                CaloriSearchUri,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int j = cursor.getCount();
        if (j == 0) {

        } else {

            while (cursor.moveToNext()) {
                totalCarbsConsumed = totalCarbsConsumed + (cursor.getFloat(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_FOOD_CARBS)));
                totalFatConsumed = totalFatConsumed + (cursor.getFloat(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_FOOD_FAT)));
                totalProteinConsumed = totalProteinConsumed + (cursor.getFloat(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_FOOD_PROTEIN)));

            }

            cursor.close();
            drawBarChart();


        }


    }


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
