package ua.com.dev_club.agrogeorgia.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.orm.SugarDb;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.activities.EmployeeDetailActivity;
import ua.com.dev_club.agrogeorgia.activities.PersonalInfoActivity;
import ua.com.dev_club.agrogeorgia.adapters.TabEmployeeAdapter;
import ua.com.dev_club.agrogeorgia.adapters.TreeEmployeeInfoList;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;
import ua.com.dev_club.agrogeorgia.models.FixedAssets;
import ua.com.dev_club.agrogeorgia.models.Project;
import ua.com.dev_club.agrogeorgia.models.Work;
import ua.com.dev_club.agrogeorgia.adapters.TreeEmployeeInfoList.FixedAssetLevel.WorkLevel.EmployeeLevel;
import ua.com.dev_club.agrogeorgia.adapters.TreeEmployeeInfoList.FixedAssetLevel;
import ua.com.dev_club.agrogeorgia.adapters.TreeEmployeeInfoList.FixedAssetLevel.WorkLevel;

import static com.orm.SugarContext.getSugarContext;

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
    Boolean isFirstViewClick=false,isSecondViewClick=false,isThirdViewClick=false;
    LinearLayout mLinearListView;

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
        calenderRecyclerView.setVisibility(View.GONE);


        tabCalenderAdapter.notifyDataSetChanged();

        drawTreeEmploeeInfo(newText);
    }

    public void drawTreeEmploeeInfo(String query){

        mLinearListView = (LinearLayout) mRootView.findViewById(R.id.linear_listview);

        if(mLinearListView.getChildCount() > 0)
            mLinearListView.removeAllViews();

        HashSet<Project> projectsSet = new HashSet();
        HashSet<FixedAssets> fixedAssetsSet = new HashSet();
        HashSet<Work> worksSet = new HashSet();

        for (ComplexWork complexWork : complexWorkList){
            projectsSet.add(complexWork.getProject());
            fixedAssetsSet.add(complexWork.getFixedAssets());
            worksSet.add(complexWork.getWork());
        }


        ArrayList<TreeEmployeeInfoList>projectArrayList=new ArrayList<TreeEmployeeInfoList>();;
        for (Project project:projectsSet){
            ArrayList<FixedAssetLevel> fixedAssetsArrayList=new ArrayList<FixedAssetLevel>();
            for (FixedAssets fixedAssets: fixedAssetsSet){
                ArrayList<WorkLevel> workArrayList=new ArrayList<WorkLevel>();
                for(Work work:worksSet){
                    List<ComplexWork> complexWorkListEmployee = ComplexWork.findWithQuery(ComplexWork.class, "Select * from Complex_work where finished=1 and Project=? and fixed_Assets=? and work=? and year=? and month=?", project.getId().toString(),fixedAssets.getId().toString(),work.getId().toString(),String.valueOf(year),String.valueOf(Integer.valueOf(month)));
                    ArrayList<EmployeeLevel> employeeArrayList=new ArrayList<EmployeeLevel>();
                    for(ComplexWork complexWork:complexWorkListEmployee){
                        if(!queryContainsEmployee(complexWork,query)) continue;
                        employeeArrayList.add(new EmployeeLevel(complexWork.getEmployee().getEmployeeName(), complexWork.getHours(),complexWork.getEmployee().getId().toString()));
                    }
                    if(employeeArrayList.isEmpty()) continue;
                    workArrayList.add(new WorkLevel(work.getWorkName(), employeeArrayList));
                }
                if(workArrayList.isEmpty()) continue;
                fixedAssetsArrayList.add(new FixedAssetLevel(fixedAssets.getFixedAssetsName(), workArrayList));
            }
            if(fixedAssetsArrayList.isEmpty()) continue;
            projectArrayList.add(new TreeEmployeeInfoList(project.getProjectName(), fixedAssetsArrayList));
        }



        /***
         * adding item into listview
         * project
         */
        for (int i = 0; i < projectArrayList.size(); i++) {

            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.row_first, null);

            final TextView projectNameView = (TextView) mLinearView.findViewById(R.id.textViewName);
            final RelativeLayout mLinearFirstArrow=(RelativeLayout)mLinearView.findViewById(R.id.linearFirst);
            final ImageView mImageArrowFirst=(ImageView)mLinearView.findViewById(R.id.imageFirstArrow);
            final ImageView mImageArrowFirstProject=(ImageView)mLinearView.findViewById(R.id.imageProject);
            final LinearLayout mLinearScrollSecond=(LinearLayout)mLinearView.findViewById(R.id.linear_scroll);

            if(isFirstViewClick==false){
                mLinearScrollSecond.setVisibility(View.GONE);
                mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
            }
            else{
                mLinearScrollSecond.setVisibility(View.VISIBLE);
                mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
            }
            mImageArrowFirstProject.setBackgroundResource(R.drawable.ic_project);
            mLinearFirstArrow.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(isFirstViewClick==false){
                        isFirstViewClick=true;
                        mImageArrowFirst.setBackgroundResource(R.drawable.arw_down);
                        mLinearScrollSecond.setVisibility(View.VISIBLE);

                    }else{
                        isFirstViewClick=false;
                        mImageArrowFirst.setBackgroundResource(R.drawable.arw_lt);
                        mLinearScrollSecond.setVisibility(View.GONE);
                    }
                    return false;
                }
            });


            final String name = projectArrayList.get(i).getProjectName();
            projectNameView.setText(name);

            /**
             * fixed asset
             */
            for (int j = 0; j < projectArrayList.get(i).getFixedAssetList().size(); j++) {

                LayoutInflater inflater2 = null;
                inflater2 = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView2 = inflater2.inflate(R.layout.row_second, null);

                TextView fixedAssetsNameView = (TextView) mLinearView2.findViewById(R.id.textViewTitle);
                final RelativeLayout mLinearSecondArrow=(RelativeLayout)mLinearView2.findViewById(R.id.linearSecond);
                final ImageView mImageArrowSecond=(ImageView)mLinearView2.findViewById(R.id.imageSecondArrow);
                final LinearLayout mLinearScrollThird=(LinearLayout)mLinearView2.findViewById(R.id.linear_scroll_third);

                if(isSecondViewClick==false){
                    mLinearScrollThird.setVisibility(View.GONE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
                }
                else{
                    mLinearScrollThird.setVisibility(View.VISIBLE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.arw_down);
                }

                mLinearSecondArrow.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(isSecondViewClick==false){
                            isSecondViewClick=true;
                            mImageArrowSecond.setBackgroundResource(R.drawable.arw_down);
                            mLinearScrollThird.setVisibility(View.VISIBLE);

                        }else{
                            isSecondViewClick=false;
                            mImageArrowSecond.setBackgroundResource(R.drawable.arw_lt);
                            mLinearScrollThird.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });


                final String catName = projectArrayList.get(i).getFixedAssetList().get(j).getFixedAssetName();
                fixedAssetsNameView.setText(catName);

                /**
                 * work
                 */
                for (int k = 0; k < projectArrayList.get(i).getFixedAssetList().get(j).getWorkLevelList().size(); k++) {

                    LayoutInflater inflater3 = null;
                    inflater3 = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mLinearView3 = inflater3.inflate(R.layout.row_third, null);

                    TextView workNameView = (TextView) mLinearView3.findViewById(R.id.textViewTitle);
                    final RelativeLayout mLinearThirdArrow = (RelativeLayout) mLinearView3.findViewById(R.id.linearThird);
                    final ImageView mImageArrowThird = (ImageView) mLinearView3.findViewById(R.id.imageThirdArrow);
                    final LinearLayout mLinearScrollFourth = (LinearLayout) mLinearView3.findViewById(R.id.linear_scroll_fourth);

                    if (isThirdViewClick == false) {
                        mLinearScrollFourth.setVisibility(View.GONE);
                        mImageArrowThird.setBackgroundResource(R.drawable.arw_lt);
                    } else {
                        mLinearScrollFourth.setVisibility(View.VISIBLE);
                        mImageArrowThird.setBackgroundResource(R.drawable.arw_down);
                    }

                    mLinearThirdArrow.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (isThirdViewClick == false) {
                                isThirdViewClick = true;
                                mImageArrowThird.setBackgroundResource(R.drawable.arw_down);
                                mLinearScrollFourth.setVisibility(View.VISIBLE);

                            } else {
                                isThirdViewClick = false;
                                mImageArrowThird.setBackgroundResource(R.drawable.arw_lt);
                                mLinearScrollFourth.setVisibility(View.GONE);
                            }
                            return false;
                        }
                    });


                    final String workName = projectArrayList.get(i).getFixedAssetList().get(j).getWorkLevelList().get(k).getWorkName();
                    workNameView.setText(workName);
                    /**
                     * employee
                     */
                    for (int l = 0; l < projectArrayList.get(i).getFixedAssetList().get(j).getWorkLevelList().get(k).getEmployeeLevelList().size(); l++) {

                        LayoutInflater inflater4 = null;
                        inflater4 = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mLinearView4 = inflater4.inflate(R.layout.row_fourth, null);

                        TextView employeeNameView = (TextView) mLinearView4.findViewById(R.id.textViewItemName);
                        TextView hoursNameView = (TextView) mLinearView4.findViewById(R.id.textViewItemHours);
                        TextView employeeIDView = (TextView) mLinearView4.findViewById(R.id.textViewEmployeeID);

                        final String employeeName = projectArrayList.get(i).getFixedAssetList().get(j).getWorkLevelList().get(k).getEmployeeLevelList().get(l).getEmployeeName();
                        final String employeeHours = String.valueOf(projectArrayList.get(i).getFixedAssetList().get(j).getWorkLevelList().get(k).getEmployeeLevelList().get(l).getHours());
                        final String employeeID = projectArrayList.get(i).getFixedAssetList().get(j).getWorkLevelList().get(k).getEmployeeLevelList().get(l).getEmployeeID();

                        employeeNameView.setText(employeeName);
                        hoursNameView.setText(employeeHours);
                        employeeIDView.setText(employeeID);

                        mLinearView4.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                LinearLayout currentLayout = (LinearLayout) v;
                                TextView employeeIDView = (TextView) currentLayout.findViewById(R.id.textViewEmployeeID);
                                String employeeID = employeeIDView.getText().toString();
                                Intent intent = new Intent(getActivity(), EmployeeDetailActivity.class);
                                intent.putExtra("employeeID", employeeID);
                                getActivity().startActivity(intent);
                            }
                        });
                        mLinearScrollFourth.addView(mLinearView4);
                    }
                    mLinearScrollThird.addView(mLinearView3);
                }
                mLinearScrollSecond.addView(mLinearView2);
            }
            mLinearListView.addView(mLinearView);
        }
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

    public boolean queryContainsEmployee(ComplexWork complexWork,String query){
        if(query==null) return true;
        final String employee = complexWork.getEmployee().getEmployeeName().toLowerCase();
        return employee.contains(query);
    }
}
