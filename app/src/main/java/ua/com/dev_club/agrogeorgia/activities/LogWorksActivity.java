package ua.com.dev_club.agrogeorgia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.fragments.LogWorksFragment;

/**
 * Created by Taras on 09.04.2016.
 */
public class LogWorksActivity extends AppCompatActivity{

    ImageView exit_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logworks_activity);


        openFragment(new LogWorksFragment());

        exit_button = (ImageView) findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sample_content_fragment, fragment)
                .commit();
    }


}
