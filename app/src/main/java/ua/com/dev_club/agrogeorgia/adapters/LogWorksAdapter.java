package ua.com.dev_club.agrogeorgia.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.helpers.LogWorkHelper;
import ua.com.dev_club.agrogeorgia.models.LogWork;
import ua.com.dev_club.agrogeorgia.models.Work;
import ua.com.dev_club.agrogeorgia.models.WorkResult;

/**
 * Created by Taras on 06.04.2016.
 */
public class LogWorksAdapter extends RecyclerView.Adapter<LogWorksAdapter.LogWorkHolder>{

    public Context context;
    public View rowView;

    @Override
    public LogWorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.logworks_list_row, parent, false);

        LogWorkHolder viewHolder = new LogWorkHolder(rowView, new MyCustomEditTextListener());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LogWorkHolder holder, int position) {
        final WorkResult selectedWork = LogWorkHelper.getLogWork().getWorkList().get(position);
        holder.work_name.setText(selectedWork.getWork().getWorkName());
        holder.quantity.setText(selectedWork.getResult().toString());
        holder.myCustomEditTextListener.updatePosition(position);
    }

    @Override
    public int getItemCount() {
        return LogWorkHelper.getLogWork().getWorkList().size();
    }

    public static class LogWorkHolder extends RecyclerView.ViewHolder  {

        public View root;
        public TextView work_name;
        public EditText quantity;
        public MyCustomEditTextListener myCustomEditTextListener;

        public LogWorkHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
            super(v);

            this.root = v.findViewById(R.id.root);
            this.work_name = (TextView)v.findViewById(R.id.work_name);
            this.quantity = (EditText) v.findViewById(R.id.quantity);
            this.myCustomEditTextListener = myCustomEditTextListener;

            this.quantity.addTextChangedListener(myCustomEditTextListener);


        }

    }

    public LogWorksAdapter(Context context){
        this.context = context;
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //mDataset[position] = charSequence.toString();
            String quantity = charSequence.toString();
            if (!quantity.equals(""))
            LogWorkHelper.getLogWork().getWorkList().get(position).setResult(Integer.valueOf(quantity));
        }

        @Override
        public void afterTextChanged(Editable editable) {
        /*    int adapterPosition =  holder.getAdapterPosition();
            logWork.getWorkList().get(adapterPosition).setResult(selectedWork.getResult());*/
        }
    }
}
