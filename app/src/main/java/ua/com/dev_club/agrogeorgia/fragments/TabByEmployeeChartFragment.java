package ua.com.dev_club.agrogeorgia.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.adapters.TabEmployeeAdapter;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;

/**
 * Created by 1cspe on 26.04.2016.
 */
public class TabByEmployeeChartFragment extends Fragment {

    Context context;

    View mRootView;
    RecyclerView calenderRecyclerView;
    RecyclerView.Adapter tabCalenderAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private LineChart[] mCharts = new LineChart[1];
    private Typeface mTf;
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private int[] mColors = new int[] {
            Color.rgb(180, 230, 180),
            Color.rgb(240, 240, 30),
            Color.rgb(89, 199, 250),
            Color.rgb(250, 104, 104)
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_colored_lines, container, false);



        //updateAdapter(String.valueOf(year), String.valueOf(month + 1));
        /////////////////////////////////
        mCharts[0] = (LineChart) mRootView.findViewById(R.id.chart1);
        /*mCharts[1] = (LineChart) mRootView.findViewById(R.id.chart2);
        mCharts[2] = (LineChart) mRootView.findViewById(R.id.chart3);
        mCharts[3] = (LineChart) mRootView.findViewById(R.id.chart4);*/

//        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");

        for (int i = 0; i < mCharts.length; i++) {

            // TODO vtm getting data

            LineData data = getEmployeeData(36, 100);
        //    data.setValueTypeface(mTf);

            // add some transparency to the color with "& 0x90FFFFFF"
            setupChart(mCharts[i], data, mColors[i % mColors.length]);
        }











/*
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        updateAdapter(String.valueOf(year), String.valueOf(month + 1));


*/


       /* mLayoutManager = new LinearLayoutManager(getActivity());
        calenderRecyclerView.setLayoutManager(mLayoutManager);

        tabCalenderAdapter.notifyDataSetChanged();*/


        return mRootView;
    }

    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleColor(color);

        // no description text
        chart.setDescription(getString(R.string.rate));
        chart.setNoDataTextDescription(getString(R.string.rate));

        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);

        XAxis x = chart.getXAxis();
        //y.setLabelCount(6, true);
        x.setTextColor(Color.WHITE);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.WHITE);
        x.setEnabled(true);

        x.setDrawLabels(true);
        //l.setEnabled(false);

        //chart.getXAxis().setEnabled(true);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    private LineData getData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i % 12]);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setColor(Color.WHITE);
        set1.setCircleColorHole(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        return data;
    }

    private LineData getEmployeeData(int count, float range) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1");
        //List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1 and year = ? and month = ?", String.valueOf(year) , String.valueOf(Integer.valueOf(month)-1));
        Total total = new Total();
        for (ComplexWork complexWork:complexWorkList){
            total.addItem(new ComplexWorkItem(complexWork, complexWork.getHours(), true));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < total.getItems().size(); i++) {
            final ComplexWorkItem selectedWork = (ComplexWorkItem) total.getItems().get(i);
            xVals.add(selectedWork.getComplexWork().getEmployee().getEmployeeName());
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < total.getItems().size(); i++) {
            float val = (float) (Math.random() * range) + 3;
            final ComplexWorkItem selectedWork = (ComplexWorkItem) total.getItems().get(i);

            yVals.add(new Entry(selectedWork.getComplexWork().getHours().floatValue(), i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setColor(Color.WHITE);
        set1.setCircleColorHole(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setHighlightEnabled(true);
        data.setDrawValues(true);


        return data;
    }


    public void updateAdapter(String year, String month){

        // List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1");
        List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1 and year = ? and month = ?", year , String.valueOf(Integer.valueOf(month)-1));
        Total total = new Total();
        for (ComplexWork complexWork:complexWorkList){
            total.addItem(new ComplexWorkItem(complexWork, complexWork.getHours(), true));
        }






        tabCalenderAdapter = new TabEmployeeAdapter(total, context);
        calenderRecyclerView.setAdapter(tabCalenderAdapter);


        tabCalenderAdapter.notifyDataSetChanged();
    }

}
