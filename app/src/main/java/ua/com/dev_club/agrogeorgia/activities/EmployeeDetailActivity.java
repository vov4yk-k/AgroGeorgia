package ua.com.dev_club.agrogeorgia.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.adapters.DaysAdapter;
import ua.com.dev_club.agrogeorgia.fragments.EmployeeFragment;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;
import ua.com.dev_club.agrogeorgia.models.Employee;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by Taras on 09.04.2016.
 */
public class EmployeeDetailActivity extends AppCompatActivity{

    Context context;
    RecyclerView daysRecyclerView;
    RecyclerView.Adapter daysAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Bundle savedInstanceState;

    String currentEmployeeID;

    Button nextMonthButton, previousMonthButton;

    TextView periodDescription, totalDescription;

    int currentMonth, currentYear;

    public static SharedPreferences prefs;
    LocalCredentialStore localCredentialStore ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity);

        ///////////     Get current Employee
        getActivityPreferences();
        ///////////////////////////////////

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);

        periodDescription = (TextView) findViewById(R.id.period_description);
        totalDescription = (TextView) findViewById(R.id.total_textView);
        setPeriodDescription(String.valueOf(currentYear), String.valueOf(currentMonth));


        daysRecyclerView = (RecyclerView) findViewById(R.id.daysRecyclerView);
        daysRecyclerView.setItemViewCacheSize(31);

        //projectsRecyclerView.setAdapter(daysAdapter);

        updateAdapter(String.valueOf(currentYear), String.valueOf(currentMonth + 1));

        mLayoutManager = new LinearLayoutManager(this);
        daysRecyclerView.setLayoutManager(mLayoutManager);

        daysAdapter.notifyDataSetChanged();

        nextMonthButton = (Button) findViewById(R.id.next_period_button);
        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMonth();
            }
        });

        previousMonthButton = (Button) findViewById(R.id.previous_period_button);
        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusMonth();
            }
        });

    }

    private void getActivityPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        localCredentialStore = new LocalCredentialStore(prefs);

        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                currentEmployeeID = null;
            } else {
                currentEmployeeID = extras.getString("employeeID");
            }
        } else {
            currentEmployeeID = (String) savedInstanceState.getSerializable("employeeID");
        }

        if (currentEmployeeID==null){
            currentEmployeeID = localCredentialStore.getEmployeeID();
        }else {
            localCredentialStore.storeEmployeeID(currentEmployeeID);
        }
    }

    private void setPeriodDescription(String currentYear, String currentMonth) {
        periodDescription.setText(String.valueOf(currentYear) + " --- " + String.valueOf(currentMonth) );
    }

    private void setTotal(Double total){
        totalDescription.setText( "Total: " + total );
    }

    public void addMonth(){
        Calendar c = Calendar.getInstance();
        c.set(currentYear, currentMonth, 1);
        c.add(Calendar.MONTH, 1);

        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);

        updateAdapter(String.valueOf(currentYear), String.valueOf(currentMonth + 1));
    };

    @Override
    protected void onResume() {
        super.onResume();
        ///////////     Get current Employee
        currentEmployeeID = localCredentialStore.getEmployeeID();
    }

    public void minusMonth(){
        Calendar c = Calendar.getInstance();
        c.set(currentYear, currentMonth, 1);
        c.add(Calendar.MONTH, -1);

        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);

        updateAdapter(String.valueOf(currentYear), String.valueOf(currentMonth + 1));
    };

    public int getMonth(int month){
       return month + 1;
    }

    public void updateAdapter(String year, String month){

        setPeriodDescription(year, month);

        if (currentEmployeeID==null)
            currentEmployeeID = localCredentialStore.getEmployeeID();

        List<ComplexWork> complexWorkList = ComplexWork.find(ComplexWork.class, "finished = 1 and year = ? and month = ? and employee = ?", year , String.valueOf(Integer.valueOf(month)-1), currentEmployeeID);

        Total total = new Total();
        for (ComplexWork complexWork:complexWorkList){
            total.addItem(new ComplexWorkItem(complexWork, complexWork.getHours()));
        }

        GregorianCalendar mycal = new GregorianCalendar(Integer.valueOf(year), Integer.valueOf(month), 1);
        Integer daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        daysAdapter = new DaysAdapter(total, context, daysInMonth);
        daysRecyclerView.setAdapter(daysAdapter);

        daysAdapter.notifyDataSetChanged();

        setTotal(total.getTotal());
    }



}
