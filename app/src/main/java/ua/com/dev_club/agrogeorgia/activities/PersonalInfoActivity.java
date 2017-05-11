package ua.com.dev_club.agrogeorgia.activities;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.fragments.LogWorksFragment;
import ua.com.dev_club.agrogeorgia.fragments.TabByDateFragment;
import ua.com.dev_club.agrogeorgia.fragments.TabByEmployeeChartFragment;
import ua.com.dev_club.agrogeorgia.fragments.TabByEmployeeFragment;

/**
 * Created by 1cspe on 26.04.2016.
 */
public class PersonalInfoActivity extends AppCompatActivity  {

    MaterialTabHost tabHost;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    String[] monthList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String[] dayList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                        "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                        "25", "26", "27", "28", "29", "30", "31"};

    //String[] yearList = {"2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};

    String[] yearList;

    private String fragmentQuery;

    String currentMonth, currentYear;

    Spinner monthSpinner, yearSpinner;
    Button applyFilterButton;

    TabByEmployeeFragment tabByEmployeeFragment;
    TabByDateFragment tabByDateFragment;

    Resources res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_layout);

        setFragmentQuery("");

        createFragments();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();
                if(tabName.equals(getString(R.string.by_employee))){
                    findViewById(R.id.tabByDateRecyclerView).setVisibility(View.GONE);
                }else {
                    findViewById(R.id.tabByDateRecyclerView).setVisibility(View.VISIBLE);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        monthSpinner = (Spinner)findViewById(R.id.monthSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        setCurrentDateForSpinner(adapter,monthSpinner,Calendar.MONTH);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMonth = monthList[position];
                tabByDateFragment.updateAdapter(yearSpinner.getSelectedItem().toString(), monthSpinner.getSelectedItem().toString());
                tabByEmployeeFragment.updateAdapter(yearSpinner.getSelectedItem().toString(), monthSpinner.getSelectedItem().toString(), getFragmentQuery());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        prepareYears();

        yearSpinner = (Spinner)findViewById(R.id.yearSpinner);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, yearList);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        setCurrentDateForSpinner(yearAdapter,yearSpinner,Calendar.YEAR);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentYear = yearList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setCurrentDateForSpinner(ArrayAdapter<String> adapter, Spinner spinner, int partDate){
        Calendar rightNow = Calendar.getInstance();
        int currentPartOfDate = rightNow.get(partDate);
        currentPartOfDate += partDate == Calendar.MONTH ? 1 : 0;
        int currentPosition = adapter.getPosition(String.valueOf(currentPartOfDate));
        spinner.setSelection(currentPosition);
    }

    public String[] prepareYears(){
        List<String> yearStringList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        year = year-2;
        for (int i=0; i<=10; i++){
            yearStringList.add(String.valueOf(year+i));
        }

        yearList = (String[]) yearStringList.toArray(new String[yearStringList.size()]);

        return yearList;
    }

    public void createFragments(){
        tabByDateFragment = new TabByDateFragment();
        tabByEmployeeFragment = new TabByEmployeeFragment();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(tabByEmployeeFragment, getString(R.string.by_employee));
        adapter.addFragment(tabByDateFragment, getString(R.string.by_period));
        adapter.addFragment(new TabByEmployeeChartFragment(), getString(R.string.by_chart));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public String getFragmentQuery() {
        return fragmentQuery;
    }

    public void setFragmentQuery(String fragmentQuery) {
        this.fragmentQuery = fragmentQuery;
    }

}
