package ua.com.dev_club.agrogeorgia.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.activities.PersonalInfoActivity;
import ua.com.dev_club.agrogeorgia.adapters.TabCalenderAdapter;
import ua.com.dev_club.agrogeorgia.adapters.TabEmployeeAdapter;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;

/**
 * Created by 1cspe on 26.04.2016.
 */
public class TabByEmployeeFragment extends Fragment implements SearchView.OnQueryTextListener {



    PersonalInfoActivity activity;
    Context context;

    View mRootView;
    RecyclerView calenderRecyclerView;
    TabEmployeeAdapter tabCalenderAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    List<ComplexWork> complexWorkList;

    SearchView search;

    int year, month, day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tab_by_date, container, false);

        calenderRecyclerView = (RecyclerView) mRootView.findViewById(R.id.tabByDateRecyclerView);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        mLayoutManager = new LinearLayoutManager(getActivity());
        calenderRecyclerView.setLayoutManager(mLayoutManager);



        search = (SearchView) mRootView.findViewById(R.id.searchView);
        search.setOnQueryTextListener(this);



        updateAdapter(String.valueOf(year), String.valueOf(month + 1), ((PersonalInfoActivity)getActivity()).getFragmentQuery());
        tabCalenderAdapter.notifyDataSetChanged();

        return mRootView;
    }


    public void updateAdapter(String year, String month, String newText){
        if (calenderRecyclerView==null)return;

        // List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1");
        complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1 and year = ? and month = ?", year , String.valueOf(Integer.valueOf(month)-1));

        complexWorkList = filter(complexWorkList, newText);

        Total total = new Total();

        for (ComplexWork complexWork:complexWorkList){
            total.addItem(new ComplexWorkItem(complexWork, complexWork.getHours(), true));
        }

        tabCalenderAdapter = new TabEmployeeAdapter(total, getActivity());


        calenderRecyclerView.setAdapter(tabCalenderAdapter);


        tabCalenderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        updateAdapter(String.valueOf(year), String.valueOf(month + 1), newText);
        return true;
    }

    private List<ComplexWork> filter(List<ComplexWork> currentComplexWorkList, String query) {
        query = query.toLowerCase();
        if (((PersonalInfoActivity)getActivity())==null)return new ArrayList<ComplexWork>();

        ((PersonalInfoActivity)getActivity()).setFragmentQuery(query);

        final List<ComplexWork> filteredModelList = new ArrayList<>();
        for (ComplexWork complexWork : currentComplexWorkList) {
            final String text = complexWork.getEmployee().getEmployeeName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(complexWork);
            }
        }
        return filteredModelList;

    }

    public void updateSearch(String newText){
        final List<ComplexWork> filteredList = filter(complexWorkList, newText);
        tabCalenderAdapter.animateTo(filteredList);
        calenderRecyclerView.scrollToPosition(0);
    }
}
