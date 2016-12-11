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

import com.appguru.android.caloriecountdown.Data.FoodContract;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class ProgressFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;
    private float total_cal;
    private static final int PROGRESS_LOADER = 0;
    ArrayList<Float> mCalList = new ArrayList<Float>();
   // private List<Float> mCalList ;
    private int dataSetSize;
    private LineChart lineChart;


    private OnFragmentInteractionListener mListener;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
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
        lineChart = new LineChart(getContext());
        View view =(lineChart);
       // View view = inflater.inflate(R.layout.fragment_progress, container, false);
        Intent intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        Log.v("fragment progress", "user:::::: " +username );

        Loader<Object> loader = getLoaderManager().getLoader(PROGRESS_LOADER);

        if (loader != null)

        {
            getLoaderManager().destroyLoader(PROGRESS_LOADER);
            getLoaderManager().initLoader(PROGRESS_LOADER, null, this);
        }
        getLoaderManager().restartLoader(PROGRESS_LOADER, null, this);

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
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, -30);
        String dateRange = df.format(c.getTime());
        Log.v("query date", "date+::" + formattedDate);
        Log.v("query date", "dateRange+::" + dateRange);

        Uri TCalorieSearchUri = FoodContract.FoodEntry.buildFoodUriWithUserIdDateRange(username,formattedDate,dateRange);

        Log.v("login activity", "inside progress create "+TCalorieSearchUri.toString() );
        return new CursorLoader(getActivity(),
                TCalorieSearchUri,
                null,
                null,
                null,
                null);
    }




    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor1) {

        int j = cursor1.getCount();
        if(j==0)
        {
            Log.v("No macro-nutrients ", ":::15 "+"zero call" );

        }
        else{
            int day_count =1;
            while (cursor1.moveToNext()) {
              // total_cal = (cursor1.getFloat(cursor1.getColumnIndex(FoodContract.FoodEntry.COLUMN_FOOD_CALORIES)));
                total_cal = (cursor1.getFloat(0));
                Log.v("calories tot ", ":::yes "+total_cal);

                mCalList.add(total_cal);
                dataSetSize = mCalList.size();
                day_count = day_count+1;
                if(day_count ==30)
                {
                    break;
                }



            }

            cursor1.close();
            drawChart();
            //drawBarChart();


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

    private void drawChart() {
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i < dataSetSize; i++) {
            xVals.add(i,String.valueOf(i+1));
            yVals.add(new Entry((mCalList.get(i)),i));
        }

        LineDataSet dataSet = new LineDataSet(yVals, "Calories per day");
        LineData lineData = new LineData(xVals, dataSet);
        lineChart.setDescription("Calories consumed on daily basis");
        dataSet.setDrawFilled(true);
        lineChart.setData(lineData);
        lineChart.setBackgroundColor(getResources().getColor(R.color.material_blue_lightest));
        lineChart.animateY(5000);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

    }
}
