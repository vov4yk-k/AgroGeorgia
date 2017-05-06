package ua.com.dev_club.agrogeorgia.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.activities.EmployeeDetailActivity;
import ua.com.dev_club.agrogeorgia.activities.MainActivity;
import ua.com.dev_club.agrogeorgia.activities.PersonalInfoActivity;
import ua.com.dev_club.agrogeorgia.helpers.Total;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.ComplexWorkItem;
import ua.com.dev_club.agrogeorgia.models.Employee;

/**
 * Created by Taras on 06.04.2016.
 */
public class TabEmployeeAdapter extends RecyclerView.Adapter<TabEmployeeAdapter.ViewHolder>{



    public Total mComplexWorks;
    public Context context;
    public View rowView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_employee_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ComplexWorkItem selectedWork = (ComplexWorkItem) mComplexWorks.getItems().get(position);
        holder.name.setText(selectedWork.getComplexWork().getEmployee().getEmployeeName()); //selectedWork.getDate()
        holder.count_selected.setText(selectedWork.getTotalHours().toString());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startEmployeeCalenderActivity(selectedWork.getComplexWork().getEmployee());

                Intent intent = new Intent(context , EmployeeDetailActivity.class);
                String employeeID = selectedWork.getComplexWork().getEmployee().getId().toString();
                intent.putExtra("employeeID", employeeID);
                context.startActivity(intent);

            }
        });
    }

    private void startEmployeeCalenderActivity(Employee employee) {

    }

    @Override
    public int getItemCount() {
        return mComplexWorks.getItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView name;
        public TextView count_selected;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView)itemView.findViewById(R.id.tab_employee_name);
            this.count_selected = (TextView)itemView.findViewById(R.id.count_selected);
        }
    }

    public TabEmployeeAdapter(Total complexWorkArrayList, Context context){
        this.mComplexWorks = complexWorkArrayList;
        this.context = context;
    }


    // additional methods for search animation
    public Vector removeItem(int position) {
        final Vector complexWork = (Vector) mComplexWorks.getItems().remove(position);
        notifyItemRemoved(position);
        return complexWork;
    }

    public void addItem(int position, ComplexWork complexWork) {
        mComplexWorks.getItems().add(position, complexWork);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ComplexWork model = (ComplexWork) mComplexWorks.getItems().remove(fromPosition);
        if (mComplexWorks.getItems().size()<toPosition) {return;}
        mComplexWorks.getItems().add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<ComplexWork> complexWorkList) {
        applyAndAnimateRemovals(complexWorkList);
        applyAndAnimateAdditions(complexWorkList);
        applyAndAnimateMovedItems(complexWorkList);
    }

    private void applyAndAnimateRemovals(List<ComplexWork> newModels) {
        for (int i = mComplexWorks.getItems().size() - 1; i >= 0; i--) {
            final ComplexWork model = (ComplexWork) mComplexWorks.getItems().get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ComplexWork> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ComplexWork model = newModels.get(i);
            if (!mComplexWorks.getItems().contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ComplexWork> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ComplexWork model = newModels.get(toPosition);
            final int fromPosition = mComplexWorks.getItems().indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
