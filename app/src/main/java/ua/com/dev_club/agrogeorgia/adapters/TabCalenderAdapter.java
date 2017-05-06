package ua.com.dev_club.agrogeorgia.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.activities.CalenderDetailActivity;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;
import ua.com.dev_club.agrogeorgia.models.Work;

/**
 * Created by Taras on 06.04.2016.
 */
public class TabCalenderAdapter extends RecyclerView.Adapter<TabCalenderAdapter.ViewHolder>{

    public Total mComplexWorks;
    public Context context;
    public View rowView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ComplexWorkItem selectedWork = (ComplexWorkItem) mComplexWorks.getItems().get(position);
        holder.date.setText(selectedWork.getComplexWork().getDate()); //selectedWork.getDate()
        holder.count_selected.setText(selectedWork.getTotalHours().toString());

        holder.date.setOnClickListener(v -> {
            Intent intent = new Intent(context , CalenderDetailActivity.class);
            Integer currentYear = selectedWork.getComplexWork().getYear();
            Integer currentMonth = selectedWork.getComplexWork().getMonth();
            Integer currentDay = selectedWork.getComplexWork().getDay();
            intent.putExtra("currentYear", currentYear);
            intent.putExtra("currentMonth", currentMonth);
            intent.putExtra("currentDay", currentDay);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mComplexWorks.getItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView date;
        public TextView count_selected;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = (TextView)itemView.findViewById(R.id.date);
            this.count_selected = (TextView)itemView.findViewById(R.id.count_selected);
        }
    }

    public TabCalenderAdapter(Total complexWorkArrayList, Context context){
        this.mComplexWorks = complexWorkArrayList;
        this.context = context;
    }
}
