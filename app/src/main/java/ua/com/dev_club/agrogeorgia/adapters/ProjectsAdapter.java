package ua.com.dev_club.agrogeorgia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import ua.com.dev_club.agrogeorgia.models.Work;

/**
 * Created by Taras on 27.04.2016.
 */
public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    public ArrayList<ComplexWork> mComplexWorks;
    public Integer daysInMonth;
    public Context context;
    public View rowView;

    @Override
    public ProjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectsAdapter.ViewHolder holder, int position) {

        ComplexWork selectedWork = mComplexWorks.get(position);
        //ComplexWorkItem selectedWork = currentDayStatistic.getComplexWorkItem();

            holder.employeeTextView.setText(selectedWork.getEmployee().getEmployeeName());
        holder.projectTextView.setText(selectedWork.getProject().getProjectName());
        Work work = selectedWork.getWork();
        if (work!=null)holder.workTextView.setText(work.getWorkName());
            holder.hoursTextView.setText(selectedWork.getHours().toString());
        //}

       // holder.daysTextView.setText(String.valueOf(position+1));
       // }

       /* final ComplexWorkItem selectedWork = (ComplexWorkItem) mComplexWorks.getItems().get(position);
        holder.employeeTextView.setText(selectedWork.getComplexWork().getEmployee().getEmployeeName()); //selectedWork.getDate()
        holder.hoursTextView.setText(selectedWork.getTotalHours().toString());*/
        holder.itemView.setTag(String.valueOf(position));//mComplexWorks.getItems().get(position));
    }



    @Override
    public int getItemCount() {
        return mComplexWorks.size();
    }

    public long getItemId(int position) {
        //Object item =  mComplexWorks.getItems().get(position);
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView employeeTextView, projectTextView, workTextView, hoursTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            employeeTextView = (TextView) itemView.findViewById(R.id.employee_name);
            projectTextView = (TextView) itemView.findViewById(R.id.project_name);
            workTextView = (TextView) itemView.findViewById(R.id.work_name);
            hoursTextView = (TextView) itemView.findViewById(R.id.hours_textView);
        }
    }

    public ProjectsAdapter(ArrayList<ComplexWork> mComplexWorks, Context context) {
        this.mComplexWorks = mComplexWorks;
        this.context = context;

       // dayStatistics = new ArrayList<>();



      /*  Vector myVector = mComplexWorks.getItems();
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
        }*/
    }
}
