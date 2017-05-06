package ua.com.dev_club.agrogeorgia.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;
import ua.com.dev_club.agrogeorgia.models.DayStatistic;

/**
 * Created by Taras on 27.04.2016.
 */
public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    public Total mComplexWorks;
    public Integer daysInMonth;
    public Context context;
    public View rowView;
    public ArrayList<DayStatistic> dayStatistics;

    @Override
    public DaysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DaysAdapter.ViewHolder holder, int position) {

        DayStatistic currentDayStatistic = dayStatistics.get(position);
        ComplexWorkItem selectedWork = currentDayStatistic.getComplexWorkItem();

        //for(int i=1 ; i<=daysInMonth; i++) {
        if (selectedWork!=null){
            holder.employeeTextView.setText(selectedWork.getComplexWork().getEmployee().getEmployeeName()); //selectedWork.getDate()
            holder.hoursTextView.setText(selectedWork.getTotalHours().toString());
        }

        holder.daysTextView.setText(String.valueOf(position+1));
       // }

       /* final ComplexWorkItem selectedWork = (ComplexWorkItem) mComplexWorks.getItems().get(position);
        holder.employeeTextView.setText(selectedWork.getComplexWork().getEmployee().getEmployeeName()); //selectedWork.getDate()
        holder.hoursTextView.setText(selectedWork.getTotalHours().toString());*/
        holder.itemView.setTag(String.valueOf(position));//mComplexWorks.getItems().get(position));
    }



    @Override
    public int getItemCount() {
        return daysInMonth;
    }

    public long getItemId(int position) {
        //Object item =  mComplexWorks.getItems().get(position);
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView employeeTextView, daysTextView, hoursTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            employeeTextView = (TextView) itemView.findViewById(R.id.employee_name);
            daysTextView = (TextView) itemView.findViewById(R.id.day);
            hoursTextView = (TextView) itemView.findViewById(R.id.count_selected);
        }
    }

    public DaysAdapter(Total total, Context context, Integer daysInMonth) {
        this.mComplexWorks = total;
        this.context = context;
        this.daysInMonth = daysInMonth;
        dayStatistics = new ArrayList<>();



        Vector myVector = mComplexWorks.getItems();
        for (int day=1;day<=daysInMonth;day++) {
            DayStatistic dayStatistic = new DayStatistic();
            dayStatistic.setDay(day);
            for (Object complexWorkItem : myVector) {
                ComplexWorkItem selectedWork = (ComplexWorkItem) complexWorkItem;
                if (selectedWork.getComplexWork().getDay() == day) {
                    dayStatistic.setComplexWorkItem(selectedWork);
                }
            }

            dayStatistics.add(dayStatistic);
        }
    }
}
