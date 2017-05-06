package ua.com.dev_club.agrogeorgia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.models.Employee;
import ua.com.dev_club.agrogeorgia.models.EmployeeCheck;

/**
 * Created by Taras on 06.04.2016.
 */
public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder>{

    public static ArrayList<EmployeeCheck> mEmployeeList;
    public Context context;
    public View rowView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EmployeeCheck selectedEmployee = mEmployeeList.get(position);
        holder.employee_name.setText(selectedEmployee.getEmployee().getEmployeeName());
        holder.checkEmployee.setChecked(selectedEmployee.getChecked());

        holder.checkEmployee.setOnClickListener(v -> {
            selectedEmployee.setChecked(!selectedEmployee.getChecked());
        });
    }

    @Override
    public int getItemCount() {
        return mEmployeeList.size();
    }

    public void setEmployeeArrayList(ArrayList<EmployeeCheck> employeeArrayList) {
        this.mEmployeeList = employeeArrayList;
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

    public EmployeesAdapter(ArrayList<EmployeeCheck> employeeList, Context context){
       /* ArrayList<EmployeeCheck> employeeCheckArrayList = new ArrayList<>();
        for (Employee employee:employeeList)
            employeeCheckArrayList.add(new EmployeeCheck(employee, false));*/

        this.mEmployeeList = employeeList;
        this.context = context;
    }

    // additional methods for search animation
    public EmployeeCheck removeItem(int position) {
        final EmployeeCheck employeeCheck = mEmployeeList.remove(position);
        notifyItemRemoved(position);
        return employeeCheck;
    }

    public void addItem(int position, EmployeeCheck product) {
        mEmployeeList.add(position, product);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final EmployeeCheck model = mEmployeeList.remove(fromPosition);
        if (mEmployeeList.size()<toPosition) {return;}
        mEmployeeList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<EmployeeCheck> employeeChecks) {
        applyAndAnimateRemovals(employeeChecks);
        applyAndAnimateAdditions(employeeChecks);
        applyAndAnimateMovedItems(employeeChecks);
    }

    private void applyAndAnimateRemovals(List<EmployeeCheck> newModels) {
        for (int i = mEmployeeList.size() - 1; i >= 0; i--) {
            final EmployeeCheck model = mEmployeeList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<EmployeeCheck> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final EmployeeCheck model = newModels.get(i);
            if (!mEmployeeList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<EmployeeCheck> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final EmployeeCheck model = newModels.get(toPosition);
            final int fromPosition = mEmployeeList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
