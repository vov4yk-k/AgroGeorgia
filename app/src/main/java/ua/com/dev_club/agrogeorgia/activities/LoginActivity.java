package ua.com.dev_club.agrogeorgia.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.api.Constants;
import ua.com.dev_club.agrogeorgia.models.Project;
import ua.com.dev_club.agrogeorgia.models.User;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by Taras on 31.03.2016.
 */
public class LoginActivity extends AppCompatActivity {


    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);

    public static SharedPreferences prefs;
    LocalCredentialStore localCredentialStore ;

    ProgressDialog progressDialog;

    Button loginButton, settingsButton;
    EditText usernameEditText;
    EditText passwordEditText;

    String username;
    String password;
    Boolean isAuthorized = false;
    Boolean rememberMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        localCredentialStore = new LocalCredentialStore(prefs);
        //localCredentialStore.clear();

        /*if (!getCredentials().equals(":")){

        }*/

        loginButton = (Button)findViewById(R.id.btnLogin) ;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation(buttonClick);
                login();
            }
        });

        settingsButton = (Button)findViewById(R.id.btnSettings) ;
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startSettingsActivity();
            }
        });


        usernameEditText = (EditText) findViewById(R.id.user);
        passwordEditText = (EditText) findViewById(R.id.pwd);

        //SharedPreferences settings = getPreferences(0);
        //rememberMe = this.prefs.getBoolean("RememberMe", false);
        //username = this.prefs.getString("Login","");
        //password = this.prefs.getString("Password","");
        rememberMe = localCredentialStore.getRememberMe();
        username = localCredentialStore.getUserLogin();
        password = localCredentialStore.getUserPassword();

        CheckBox rememberMeChBx = (CheckBox)findViewById(R.id.rememberMeChBx);
        rememberMeChBx.setChecked(rememberMe);
        rememberMeChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rememberMe = b;
            }
        });
        usernameEditText.setText(username);
        passwordEditText.setText(password);
    }

    public void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void login() {
        //Log.d(TAG, "Login");

        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_NoActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        //GetTimeAsync getTimeAsync = new GetTimeAsync();
        //getTimeAsync.execute();


        LoadProjectsAsync loadProjectsAsync = new LoadProjectsAsync();
        loadProjectsAsync.execute();




        //LoadProjectsAsync loadProjectsAsync = new LoadProjectsAsync();
        //loadProjectsAsync.execute();


        // TODO: Implement your own authentication logic here.


        //setAuthorizeResult(true);

        // vtm
        // Create a very simple REST adapter which points the GitHub API endpoint.
        // UserService client = ServiceGenerator.createService(UserService.class);

        //MyAsync MyAsync = new MyAsync();
        //MyAsync.execute();



    }

    public String getTime() {
        try {
            //Make the Http connection so we can retrieve the time
            HttpClient httpclient = new DefaultHttpClient();
            // I am using yahoos api to get the time
            HttpResponse response = httpclient.execute(new HttpGet("http://developer.yahooapis.com/TimeService/V1/getTime?appid=YahooDemo"));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                // The response is an xml file and i have stored it in a string
                String responseString = out.toString();
                Log.d("Response", responseString);
                //We have to parse the xml file using any parser, but since i have to
                //take just one value i have deviced a shortcut to retrieve it
                int x = responseString.indexOf("<Timestamp>");
                int y = responseString.indexOf("</Timestamp>");
                //I am using the x + "<Timestamp>" because x alone gives only the start value
                Log.d("Response", responseString.substring(x + "<Timestamp>".length(), y));
                String timestamp = responseString.substring(x + "<Timestamp>".length(), y);
                // The time returned is in UNIX format so i need to multiply it by 1000 to use it
                Date d = new Date(Long.parseLong(timestamp) * 1000);
                Log.d("Response", d.toString());
                return d.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.d("Response", e.getMessage());
        } catch (IOException e) {
            Log.d("Response", e.getMessage());
        }
        return null;
    }


    public void setAuthorizeResult(final Boolean authorizedResult){
        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_NoActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (authorizedResult) {
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }


    protected String getCredentials() {
        return localCredentialStore.getCredentials();
    }

    //Setup the AuthParameters for API Call
    private class MyAsync extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            localCredentialStore.store(username, password);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean authorized) {
            super.onPostExecute(authorized);

        }
    }

    class LoadProjectsAsync extends AsyncTask<String, String, ArrayList<Project>> {

        private Object readProperty(int index, SoapObject item){
            PropertyInfo p = new PropertyInfo();
            item.getPropertyInfo(index, p);
            return p.getValue();
        }



        @Override
        protected ArrayList<Project> doInBackground(String... params) {
            try {

                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_GET_PROJECTS);

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((username + ":" + password).getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call( localCredentialStore.getCommonUrl() + Constants.GET_PROJECTS_SOAP_ACTION, envelope, headerList);

                SoapObject result = (SoapObject) envelope.getResponse();

                ArrayList<Project> projectArrayList = new ArrayList<>();

                for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        Project project = new Project();
                        project.setProjectID(final_object.getProperty("ProjectID").toString());
                        project.setProjectName(final_object.getProperty("ProjectName").toString());

                        List<Project> findProjects = Project.findWithQuery(Project.class, "Select * from Project where project_id = ?", project.getProjectID());
                        if (findProjects.size()==0) {
                            project.save();

                        } else {
                            project = findProjects.get(0);
                        }

                        project.save();

                        projectArrayList.add(project);
                    }
                }

                return projectArrayList;

            }catch (Exception ignored){
                ignored.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<Project> projects) {
            super.onPostExecute(projects);

           /* progressDialog.dismiss();

            if (projects.size()==0){
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT);
                return;
            }
            localCredentialStore.store(username, password);
            finish();*/

            GetUsersAsync getUsersAsync = new GetUsersAsync();
            getUsersAsync.execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class GetUsersAsync extends AsyncTask<String, String, User> {

        private Object readProperty(int index, SoapObject item){
            PropertyInfo p = new PropertyInfo();
            item.getPropertyInfo(index, p);
            return p.getValue();
        }

        @Override
        protected User doInBackground(String... params) {
            //1. Trying current user
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_GET_USERS);

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((username + ":" + password).getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call( localCredentialStore.getCommonUrl() + Constants.GET_USERS_SOAP_ACTION, envelope, headerList);

                SoapObject result = (SoapObject) envelope.getResponse();

                //ArrayList<User> projectArrayList = new ArrayList<>();

                for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        User user = new User();
                        user.setUserID(final_object.getProperty("UserID").toString());
                        user.setUserName(final_object.getProperty("UserName").toString());
                        user.setUserIsAdmin(Boolean.valueOf(final_object.getProperty("UserIsAdmin").toString()));
                        user.setCreatingEmployee(Boolean.valueOf(final_object.getProperty("CreatingEmployee").toString()));

                        if (user.getUserName().equals(username))  return user;

                    }
                }

                //return null;

            }catch (Exception ignored){
                ignored.printStackTrace();
                if (!ignored.getMessage().toString().equals("წვდომის უფლების დარღვევა Web-სერვისის ოპერაციასთან:  {http://directone.ge/DirectOneWorkLog}:DirectOneWorkLog:GetUsers()") ){
                    return null;
                }
            }

            try {

                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_GET_USERS);

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(getCredentials().getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call( localCredentialStore.getCommonUrl() + Constants.GET_USERS_SOAP_ACTION, envelope, headerList);

                SoapObject result = (SoapObject) envelope.getResponse();

                //ArrayList<User> projectArrayList = new ArrayList<>();

                for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        User user = new User();
                        user.setUserID(final_object.getProperty("UserID").toString());
                        user.setUserName(final_object.getProperty("UserName").toString());
                        user.setUserIsAdmin(Boolean.valueOf(final_object.getProperty("UserIsAdmin").toString()));
                        user.setCreatingEmployee(Boolean.valueOf(final_object.getProperty("CreatingEmployee").toString()));

                        if (user.getUserName().equals(username))  return user;

                    }
                }

                return null;

            }catch (Exception ignored){
                ignored.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            progressDialog.dismiss();

            if (user==null){
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                return;
            }

            //localCredentialStore.store(username, password);
            localCredentialStore.storeUserIsAdmin(user.getUserIsAdmin());
            localCredentialStore.storeCreatingEmployee(user.getCreatingEmployee());

            localCredentialStore.setRememberMe(rememberMe);
            localCredentialStore.storeUserCredentials(username,password);

            finish();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    class GetTimeAsync extends AsyncTask<Void, Boolean, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //Make the Http connection so we can retrieve the time
                HttpClient httpclient = new DefaultHttpClient();
                // I am using yahoos api to get the time
                HttpResponse response = httpclient.execute(new HttpGet("http://chronic.herokuapp.com/pdt/in+two+hours"));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    // The response is an xml file and i have stored it in a string
                    String responseString = out.toString();
                    Log.d("Response", responseString);
                    //We have to parse the xml file using any parser, but since i have to
                    //take just one value i have deviced a shortcut to retrieve it
                    int x = responseString.indexOf("<Timestamp>");
                    int y = responseString.indexOf("</Timestamp>");
                    //I am using the x + "<Timestamp>" because x alone gives only the start value
                    Log.d("Response", responseString.substring(x + "<Timestamp>".length(), y));
                    String timestamp = responseString.substring(x + "<Timestamp>".length(), y);
                    // The time returned is in UNIX format so i need to multiply it by 1000 to use it
                    Date d = new Date(Long.parseLong(timestamp) * 1000);
                    Log.d("Response", d.toString());
                    //return d.toString();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                Log.d("Response", e.getMessage());
            } catch (IOException e) {
                Log.d("Response", e.getMessage());
            }
            return false;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        localCredentialStore.setRememberMe(rememberMe);
        localCredentialStore.storeUserCredentials(username,password);
    }
}
