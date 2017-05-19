package ua.com.dev_club.agrogeorgia.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.api.Constants;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by Vova on 18.05.2017.
 */

public class NewEmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameView, surnameView, employeeNumberView, hiringDateView, bankAccountView;
    Button saveButton;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    private TextWatcher mTextWatcher;

    private String name, surname, number, hiringDate, bankAccount;

    SharedPreferences prefs;
    LocalCredentialStore localCredentialStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_employee_activity);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        localCredentialStore = new LocalCredentialStore(prefs);

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
                saveButton.setEnabled(false);
                PostNewEmployeeAsync postNewEmployeeAsync = new PostNewEmployeeAsync();
                postNewEmployeeAsync.execute();
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

        boolean disableButton = name.equals("")||surname.equals("")||number.equals("") ||hiringDate.equals("");

        saveButton.setEnabled(!disableButton);

    }

    class PostNewEmployeeAsync extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {

            HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                try {

                    PropertyInfo nameInfo = new PropertyInfo();
                    nameInfo.name = "Name";
                    nameInfo.type = String.class;
                    nameInfo.setValue(name);

                    PropertyInfo surnameInfo = new PropertyInfo();
                    surnameInfo.name = "Surname";
                    surnameInfo.type = String.class;
                    surnameInfo.setValue(surname);

                    PropertyInfo numberInfo = new PropertyInfo();
                    numberInfo.name = "Number";
                    numberInfo.type = String.class;
                    numberInfo.setValue(number);

                    PropertyInfo hiringDateInfo = new PropertyInfo();
                    hiringDateInfo.name = "HiringDate";
                    hiringDateInfo.type = String.class;
                    hiringDateInfo.setValue(hiringDate);

                    PropertyInfo bankAccountInfo = new PropertyInfo();
                    bankAccountInfo.name = "BankAccount";
                    bankAccountInfo.type = String.class;
                    bankAccountInfo.setValue(bankAccount);

                    PropertyInfo userIdInfo = new PropertyInfo();
                    userIdInfo.name = "UserID";
                    userIdInfo.type = String.class;
                    userIdInfo.setValue(localCredentialStore.getUserId());

                    SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_POST_NEW_EMPLOYEE);
                    customer.addProperty(nameInfo);
                    customer.addProperty(surnameInfo);
                    customer.addProperty(numberInfo);
                    customer.addProperty(hiringDateInfo);
                    customer.addProperty(bankAccountInfo);
                    customer.addProperty(userIdInfo);

                    List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                    headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode( localCredentialStore.getCredentials().getBytes())));

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.encodingStyle = SoapEnvelope.ENC;
                    envelope.setAddAdornments(false);
                    envelope.implicitTypes = false;

                    envelope.setOutputSoapObject(customer);
                    androidHttpTransport.call(localCredentialStore.getCommonUrl() + Constants.POST_NEW_EMPLOYEE_SOAP_ACTION, envelope, headerList);

                    SoapObject result = (SoapObject) envelope.getResponse();

                    /*//complexWork.setHours(Double.valueOf(currentHours));

                    if (currentWork.getType().equals("1")||currentWork.getType().equals("0")) {
                        complexWork.setHours(Double.valueOf(currentQuantityHours));
                    }else {
                        complexWork.setHours(Double.valueOf(currentHours));
                    }

                    complexWork.setFinished(true);

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                    complexWork.setDate(dt1.format(c.getTime()));

                    //complexWork.setDate(c.getTime().toString());
                    complexWork.setYear(year);
                    complexWork.setMonth(month);
                    complexWork.setDay(day);

                    complexWork.save();*/

                } catch (Exception ignored) {
                    ignored.printStackTrace();

                }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean posted) {
            super.onPostExecute(posted);
            if (posted){
                Toast.makeText(getApplicationContext(), "Posted succesfully", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Error while post", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
