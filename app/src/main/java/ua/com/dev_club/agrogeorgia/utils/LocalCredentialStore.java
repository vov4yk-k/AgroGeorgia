package ua.com.dev_club.agrogeorgia.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalCredentialStore {

    private static final String ENDPOINT_URL = "ENDPOINT_URL";

    private static final String DATABASE_NAME = "DATABASE_NAME";

    private static final String LOGIN = "LOGIN";
    private static final String IS_ADMIN = "IS_ADMIN";
    private static final String PASSWORD = "PASSWORD";
    private static final String EMPLOYEE_ID = "1";
    private static final String CURRENT_DAY = "CURRENT_DAY";
    private static final String CURRENT_YEAR = "CURRENT_YEAR";
    private static final String CURRENT_MONTH = "CURRENT_MONTH";

    private SharedPreferences prefs;

    public LocalCredentialStore(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public String getCredentials() {
        String login = prefs.getString(LOGIN, "");
        String password = prefs.getString(PASSWORD, "");
        return login + ":" + password;
    }

    public String getEndpointUrl(){
        String endpoint = prefs.getString(ENDPOINT_URL, "");
        return endpoint;
    }

    public void setEndpointUrl(String endpoint){
        Editor editor = prefs.edit();
        editor.putString(ENDPOINT_URL, endpoint);
        editor.commit();
    }

    public String getDatabaseName(){
        String db = prefs.getString(DATABASE_NAME, "");
        return db;
    }

    public void setDatabaseName(String dbname){
        Editor editor = prefs.edit();
        editor.putString(DATABASE_NAME, dbname);
        editor.commit();
    }

    public String getCommonUrl(){
        String endpoint = prefs.getString(ENDPOINT_URL, "");
        String database = prefs.getString(DATABASE_NAME, "");
        return endpoint + "/" + database + "/";
    }


    public void store(String login, String password) {
        Editor editor = prefs.edit();
        editor.putString(LOGIN, login);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public void storeUserIsAdmin(Boolean isAdmin) {
        Editor editor = prefs.edit();
        editor.putBoolean(IS_ADMIN, isAdmin);
        editor.commit();
    }

    public Boolean getUserIsAdmin(){
        Boolean isadmin = prefs.getBoolean(IS_ADMIN, false);
        return isadmin;
    }

    public String getAdminUser(){
        String adminUser = prefs.getString(LOGIN, "");
        return adminUser;
    }

    public String getAdminPass(){
        String pass = prefs.getString(PASSWORD, "");
        return pass;
    }

    public void storeEmployeeID(String employeeID){
        Editor editor = prefs.edit();
        editor.putString(EMPLOYEE_ID, employeeID);
        editor.commit();
    }

    public String getEmployeeID(){
        String employeeID = prefs.getString(EMPLOYEE_ID, "1");
        return employeeID;
    }

    public int getDay(){
        int currentDay = prefs.getInt(CURRENT_DAY, 1);
        return currentDay;
    }

    public int getYear(){
        int currentYear = prefs.getInt(CURRENT_YEAR, 2016);
        return currentYear;
    }
    public int getMonth(){
        int currentMonth = prefs.getInt(CURRENT_MONTH, 1);
        return currentMonth;
    }

    public void storeCurrentDate(int currentYear, int currentMonth, int currentDay){
        Editor editor = prefs.edit();
        editor.putInt(CURRENT_DAY, currentDay);
        editor.putInt(CURRENT_YEAR, currentYear);
        editor.putInt(CURRENT_MONTH, currentMonth);
        editor.commit();
    }

    public void clear() {
        Editor editor = prefs.edit();
        editor.remove(LOGIN);
        editor.remove(PASSWORD);
        editor.remove(EMPLOYEE_ID);
        editor.commit();
    }
}
