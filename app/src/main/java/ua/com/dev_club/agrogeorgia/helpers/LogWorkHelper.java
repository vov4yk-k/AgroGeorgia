package ua.com.dev_club.agrogeorgia.helpers;

import android.content.res.Resources;

import java.util.List;
import java.util.Vector;

import ua.com.dev_club.agrogeorgia.models.LogWork;

/**
 * Created by Taras on 10.04.2016.
 */
public class LogWorkHelper {

    private static LogWork logWork;

    public static boolean emptyLogWork(){
        return logWork==null;
    }

    public static LogWork getLogWork() {
        if(logWork == null) {
            logWork = new LogWork();
        }

        return logWork;
    }



}
