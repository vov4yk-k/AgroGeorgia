package ua.com.dev_club.agrogeorgia.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.adapters.TabCalenderAdapter;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;

/**
 * Created by 1cspe on 26.04.2016.
 */
public class TabByDateFragment extends Fragment {

    Context context;

    View mRootView;
    RecyclerView calenderRecyclerView;
    RecyclerView.Adapter tabCalenderAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    SearchView search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_by_date, container, false);

        initViews();


        return mRootView;
    }

    public void initViews(){
        calenderRecyclerView = (RecyclerView) mRootView.findViewById(R.id.tabByDateRecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        calenderRecyclerView.setLayoutManager(mLayoutManager);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        updateAdapter(String.valueOf(year), String.valueOf(month + 1));

        tabCalenderAdapter.notifyDataSetChanged();

        search = (SearchView) mRootView.findViewById(R.id.searchView);
        search.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    public void updateAdapter(String year, String month){

       // List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1");
        List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1 and year = ? and month = ?", year , String.valueOf(Integer.valueOf(month)-1));

        Total total = new Total();
        for (ComplexWork complexWork:complexWorkList){
           total.addItem(new ComplexWorkItem(complexWork, complexWork.getHours()));
        }

        tabCalenderAdapter = new TabCalenderAdapter(total, getActivity());

        if (calenderRecyclerView==null)return;

        calenderRecyclerView.setAdapter(tabCalenderAdapter);

        tabCalenderAdapter.notifyDataSetChanged();
    }
}
