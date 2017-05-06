package ua.com.dev_club.agrogeorgia.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.fragments.LogWorksFragment;

/**
 * Created by Taras on 09.04.2016.
 */
public class LogWorksActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logworks_activity);


        openFragment(new LogWorksFragment());

    }

    private void openFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sample_content_fragment, fragment)
                .commit();
    }


}
