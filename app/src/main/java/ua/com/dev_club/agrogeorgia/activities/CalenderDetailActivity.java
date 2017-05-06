package ua.com.dev_club.agrogeorgia.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.adapters.ProjectsAdapter;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by Taras on 09.04.2016.
 */
public class CalenderDetailActivity extends AppCompatActivity{

    Context context;
    RecyclerView projectsRecyclerView;
    RecyclerView.Adapter projectsAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Bundle savedInstanceState;

    String currentDate;

    Button nextPeriodButton, previousPeriodButton;

    TextView periodDescription, totalDescription;

    int currentMonth, currentYear, currentDay;

    public static SharedPreferences prefs;
    LocalCredentialStore localCredentialStore ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_detail_activity);

        ///////////     Get current Employee
        getActivityPreferences();
        ///////////////////////////////////

       /* Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.MONTH);*/

        periodDescription = (TextView) findViewById(R.id.period_description);
        totalDescription = (TextView) findViewById(R.id.total_textView);
        setPeriodDescription(String.valueOf(currentYear), String.valueOf(currentMonth), String.valueOf(currentDay));


        projectsRecyclerView = (RecyclerView) findViewById(R.id.projectsRecyclerView);
        projectsRecyclerView.setItemViewCacheSize(300);

        //projectsRecyclerView.setAdapter(daysAdapter);

        updateAdapter(String.valueOf(currentYear), String.valueOf(currentMonth + 1), String.valueOf(currentDay));

        mLayoutManager = new LinearLayoutManager(this);
        projectsRecyclerView.setLayoutManager(mLayoutManager);

        projectsAdapter.notifyDataSetChanged();

        nextPeriodButton = (Button) findViewById(R.id.next_period_button);
        nextPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDay();
            }
        });

        previousPeriodButton = (Button) findViewById(R.id.previous_period_button);
        previousPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusDay();
            }
        });

    }

    private void getActivityPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        localCredentialStore = new LocalCredentialStore(prefs);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Calendar c = Calendar.getInstance();
                currentYear = c.get(Calendar.YEAR);
                currentMonth = c.get(Calendar.MONTH);
                currentDay = c.get(Calendar.MONTH);
            } else {
                //currentDate = extras.getString("employeeID");
                currentDay = extras.getInt("currentDay");;
                currentYear = extras.getInt("currentYear");;
                currentMonth = extras.getInt("currentMonth");;
            }
        } else {
            //currentDate = (String) savedInstanceState.getSerializable("employeeID");
            Calendar c = Calendar.getInstance();
            currentYear = c.get(Calendar.YEAR);
            currentMonth = c.get(Calendar.MONTH);
            currentDay = c.get(Calendar.MONTH);
        }

        if (currentYear==0){
            currentYear = localCredentialStore.getYear();
            currentMonth = localCredentialStore.getMonth();
            currentDay = localCredentialStore.getDay();
        }else {
            localCredentialStore.storeCurrentDate(currentYear, currentMonth, currentDay);
        }
    }

    private void setPeriodDescription(String currentYear, String currentMonth, String day) {
        periodDescription.setText(String.valueOf(currentYear) + " --- " + String.valueOf(currentMonth) + " --- " + day);
    }

    private void setTotal(Double total){
        totalDescription.setText( "Total: " + total );
    }


    public void addDay(){
        Calendar c = Calendar.getInstance();
        c.set(currentYear, currentMonth, currentDay);
        c.add(Calendar.DAY_OF_MONTH, 1);

        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DAY_OF_MONTH);


        updateAdapter(String.valueOf(currentYear), String.valueOf(currentMonth + 1), String.valueOf(currentDay));
    };

    @Override
    protected void onResume() {
        super.onResume();
        ///////////     Get current Employee
       /* currentDay = localCredentialStore.getDay();
        currentMonth = localCredentialStore.getMonth();
        currentDay = localCredentialStore.getYear();*/
    }

    public void minusDay(){
        Calendar c = Calendar.getInstance();
        c.set(currentYear, currentMonth, currentDay);
        c.add(Calendar.DAY_OF_MONTH, -1);

        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DAY_OF_MONTH);

        updateAdapter(String.valueOf(currentYear), String.valueOf(currentMonth + 1), String.valueOf(currentDay));
    };



    public void updateAdapter(String year, String month, String day){

        setPeriodDescription(year, month, day);

        List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1 and year = ? and month = ? and day = ?", year , String.valueOf(Integer.valueOf(month)-1), day);

        Total total = new Total();
        for (ComplexWork complexWork:complexWorkList){
            total.addItem(new ComplexWorkItem(complexWork, complexWork.getHours()));
        }


        projectsAdapter = new ProjectsAdapter((ArrayList<ComplexWork>) complexWorkList, context);
        projectsRecyclerView.setAdapter(projectsAdapter);

        projectsAdapter.notifyDataSetChanged();

        setTotal(total.getTotal());
    }



}
