package ua.com.dev_club.agrogeorgia.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.api.Constants;
import ua.com.dev_club.agrogeorgia.models.Project;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

public class MainActivity extends AppCompatActivity {

    private Context context;

    public static SharedPreferences prefs;
    LocalCredentialStore localCredentialStore ;

    private AlphaAnimation pressAnimation = new AlphaAnimation(1F, 0.7F);


    Button employeesButton, worksButton, logWorksButton;
    Button personalManagementButton, btnPersonalInfo;

    ImageView exit_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        localCredentialStore = new LocalCredentialStore(prefs);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");


        exit_button = (ImageView) findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //localCredentialStore.clear();
                startLoginActivity();
            }
        });


        startLoginActivity();

        personalManagementButton = (Button) findViewById(R.id.btnPersonalManagement);
        personalManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalManagementButton.startAnimation(pressAnimation);
                startWorksActivity();
            }
        });

        logWorksButton = (Button) findViewById(R.id.btnWorkEnd);
        logWorksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logWorksButton.startAnimation(pressAnimation);
                startLogWorksActivity();
            }
        });

        logWorksButton = (Button) findViewById(R.id.btnWorkEnd);
        logWorksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logWorksButton.startAnimation(pressAnimation);
                startLogWorksActivity();
            }
        });

        btnPersonalInfo = (Button) findViewById(R.id.btnPersonalInfo);
        btnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPersonalInfoActivity();
            }
        });

        setPersonalInfoVisibility();

    }

    public void setPersonalInfoVisibility(){
        if (!localCredentialStore.getUserIsAdmin()) {
            btnPersonalInfo.setVisibility(View.GONE);
        }else {
            btnPersonalInfo.setVisibility(View.VISIBLE);
        }

    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void startWorksActivity(){
        Intent intent = new Intent(this, WorksActivity.class);
        startActivity(intent);
    }

    public void startLogWorksActivity(){
        Intent intent = new Intent(this, LogWorksActivity.class);
        startActivity(intent);
    }

    public void startPersonalInfoActivity(){
        Intent intent = new Intent(this, PersonalInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setPersonalInfoVisibility();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setPersonalInfoVisibility();
    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        setPersonalInfoVisibility();
    }

    /*
    public void startPersonalManagementActivity(){
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
        final DBHelper dbHelper = new DBHelper(this);
        final List<MeasurementDevice> totalDeviceList = dbHelper.getAllDevices();
        String[] deviceNameArr = new String[totalDeviceList.size()];
        final boolean[] selectedItems = new boolean[totalDeviceList.size()];
        for(int i = 0 ; i < deviceNameArr.length ; i++){
            deviceNameArr[i] = totalDeviceList.get(i).getName();
            selectedItems[i] = false;
            for(int j = 0 ; j < measurementDeviceArrayList.size() ; j++){
                if(measurementDeviceArrayList.get(j).getId() == totalDeviceList.get(i).getId()){
                    selectedItems[i] = true;
                    break;
                }
            }
        }
        alerBuilder.setSingleChoiceItems(deviceNameArr, selectedItems, new DialogInterface.On() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                Log.e("CheckStatus",String.valueOf(b));
            }
        }).setPositiveButton(R.string.ok_ww,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ii) {

                for(int i = 0 ; i < selectedItems.length ; i++)
                    Log.e("Sizzz",String.valueOf(selectedItems[i]));
            }
        }).setCancelable(false).setTitle(R.string.add_device).create().show();
    }
*/


}
