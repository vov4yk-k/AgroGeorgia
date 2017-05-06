package ua.com.dev_club.agrogeorgia.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by 1cspe on 06.06.2016.
 */
public class SettingsActivity extends AppCompatActivity {

    EditText url_edittext, adminUser_edittext, adminPass_edittext, databaseEditText;
    Button saveButton;

    SharedPreferences prefs;
    LocalCredentialStore localCredentialStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        localCredentialStore = new LocalCredentialStore(prefs);

        setContentView(R.layout.activity_settings);

        url_edittext = (EditText) findViewById(R.id.urlEditText);
        url_edittext.setText(localCredentialStore.getEndpointUrl());


        databaseEditText = (EditText) findViewById(R.id.databaseEditText);
        databaseEditText.setText(localCredentialStore.getDatabaseName());

        adminUser_edittext = (EditText) findViewById(R.id.adminUser_edittext);
        adminUser_edittext.setText(localCredentialStore.getAdminUser());

        adminPass_edittext = (EditText) findViewById(R.id.adminPass_edittext);
        adminPass_edittext.setText(localCredentialStore.getAdminPass());

        saveButton = (Button) findViewById(R.id.save_preferences_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpoint = url_edittext.getText().toString();
                String databaseName = databaseEditText.getText().toString();
                localCredentialStore.setEndpointUrl(endpoint);
                localCredentialStore.setDatabaseName(databaseName);
                localCredentialStore.store(adminUser_edittext.getText().toString(), adminPass_edittext.getText().toString());
                finish();
            }
        });

    }
}
