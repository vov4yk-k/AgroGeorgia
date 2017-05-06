package ua.com.dev_club.agrogeorgia;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Taras on 26.04.2016.
 */
public class SugarORMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}