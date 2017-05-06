package ua.com.dev_club.agrogeorgia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.models.Employee;
import ua.com.dev_club.agrogeorgia.models.Work;

/**
 * Created by Taras on 06.04.2016.
 */
public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.ViewHolder>{

    public ArrayList<Work> mWorkArrayList;
    public Context context;
    public View rowView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.works_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Work selectedWork = mWorkArrayList.get(position);
        holder.work_name.setText(selectedWork.getWorkName());


    }

    @Override
    public int getItemCount() {
        return mWorkArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView work_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.work_name = (TextView)itemView.findViewById(R.id.work_name);

        }

    }

    public WorksAdapter(ArrayList<Work> workArrayList, Context context){
        this.mWorkArrayList = workArrayList;
        this.context = context;
    }
}
