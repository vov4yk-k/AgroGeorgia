package ua.com.dev_club.agrogeorgia.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ua.com.dev_club.agrogeorgia.R;

/**
 * Created by Vova on 18.05.2017.
 */

public class NewEmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameView, surnameView, employeeNumberView, hiringDateView, bankAccountView;
    Button saveButton;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    private TextWatcher mTextWatcher;

    private String name, surname, umber, number, hiringDate, bankAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_employee_activity);

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsForEmptyValues();
            }
        };

        nameView = (EditText) findViewById(R.id.new_employee_name);
        nameView.addTextChangedListener(mTextWatcher);

        surnameView = (EditText) findViewById(R.id.new_employee_surname);
        surnameView.addTextChangedListener(mTextWatcher);

        employeeNumberView = (EditText) findViewById(R.id.new_employee_number);
        employeeNumberView.addTextChangedListener(mTextWatcher);

        hiringDateView = (EditText) findViewById(R.id.new_employee_hiring_date);
        hiringDateView.addTextChangedListener(mTextWatcher);

        bankAccountView = (EditText) findViewById(R.id.new_employee_bank_account);
        bankAccountView.addTextChangedListener(mTextWatcher);

        saveButton = (Button) findViewById(R.id.create_employee);
        saveButton.setEnabled(false);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });

        hiringDateView.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                hiringDateView.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        if (view==hiringDateView)
            datePickerDialog.show();
    }

    private void checkFieldsForEmptyValues(){

        name = nameView.getText().toString();
        surname = surnameView.getText().toString();
        number = employeeNumberView.getText().toString();
        hiringDate = hiringDateView.getText().toString();
        bankAccount = bankAccountView.getText().toString();

        boolean disableButton = name.equals("")||surname.equals("")||number.equals("")
                ||hiringDate.equals("")||bankAccount.equals("");

        saveButton.setEnabled(!disableButton);

    }

}
