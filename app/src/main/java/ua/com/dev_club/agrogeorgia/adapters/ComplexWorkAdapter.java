package ua.com.dev_club.agrogeorgia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.Employee;
import ua.com.dev_club.agrogeorgia.models.EmployeeCheck;

/**
 * Created by Taras on 06.04.2016.
 */
public class ComplexWorkAdapter extends RecyclerView.Adapter<ComplexWorkAdapter.ViewHolder>{

    public static List<ComplexWork> complexWorks;
    public Context context;
    public View rowView;

    public static List<ComplexWork> getComplexWorks() {
        return complexWorks;
    }

    public void setComplexWorks(List<ComplexWork> complexWorks) {
        this.complexWorks = complexWorks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ComplexWork selectedComplexWork = complexWorks.get(position);
        final Employee selectedEmployee = selectedComplexWork.getEmployee();
        holder.employee_name.setText(selectedEmployee.getEmployeeName());
        holder.checkEmployee.setChecked(selectedComplexWork.getFinished());

        holder.checkEmployee.setOnClickListener(v -> {
            selectedComplexWork.setFinished(!selectedComplexWork.getFinished());
            selectedComplexWork.save();
            //selectedEmployee.setChecked(!selectedEmployee.getChecked());
        });
    }

    @Override
    public int getItemCount() {
        return complexWorks.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView employee_name;
        public CheckBox checkEmployee;

        public ViewHolder(View itemView) {
            super(itemView);
            this.employee_name = (TextView)itemView.findViewById(R.id.employee_name);
            this.checkEmployee = (CheckBox)itemView.findViewById(R.id.checkEmployee);
        }

    }

    public ComplexWorkAdapter(List<ComplexWork> complexWorks, Context context){
       /* ArrayList<EmployeeCheck> employeeCheckArrayList = new ArrayList<>();
        for (Employee employee:employeeList)
            employeeCheckArrayList.add(new EmployeeCheck(employee, false));*/

        this.complexWorks = complexWorks;
        this.context = context;
    }

    // additional methods for search animation
    public ComplexWork removeItem(int position) {
        final ComplexWork complexWork = complexWorks.remove(position);
        notifyItemRemoved(position);
        return complexWork;
    }

    public void addItem(int position, ComplexWork complexWork) {
        complexWorks.add(position, complexWork);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ComplexWork model = complexWorks.remove(fromPosition);
        if (complexWorks.size()<toPosition) {return;}
        complexWorks.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<ComplexWork> complexWorkList) {
        applyAndAnimateRemovals(complexWorkList);
        applyAndAnimateAdditions(complexWorkList);
        applyAndAnimateMovedItems(complexWorkList);
    }

    private void applyAndAnimateRemovals(List<ComplexWork> newModels) {
        for (int i = complexWorks.size() - 1; i >= 0; i--) {
            final ComplexWork model = complexWorks.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ComplexWork> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ComplexWork model = newModels.get(i);
            if (!complexWorks.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ComplexWork> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ComplexWork model = newModels.get(toPosition);
            final int fromPosition = complexWorks.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
