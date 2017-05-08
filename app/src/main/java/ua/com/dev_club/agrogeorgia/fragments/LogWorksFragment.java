package ua.com.dev_club.agrogeorgia.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.activities.WorksActivity;
import ua.com.dev_club.agrogeorgia.adapters.ComplexWorkAdapter;
import ua.com.dev_club.agrogeorgia.adapters.EmployeesAdapter;
import ua.com.dev_club.agrogeorgia.adapters.LogWorksAdapter;
import ua.com.dev_club.agrogeorgia.adapters.WorksAdapter;
import ua.com.dev_club.agrogeorgia.api.Constants;
import ua.com.dev_club.agrogeorgia.helpers.LogWorkHelper;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.Employee;
import ua.com.dev_club.agrogeorgia.models.EmployeeCheck;
import ua.com.dev_club.agrogeorgia.models.FixedAssets;
import ua.com.dev_club.agrogeorgia.models.LogWork;
import ua.com.dev_club.agrogeorgia.models.Project;
import ua.com.dev_club.agrogeorgia.models.Work;
import ua.com.dev_club.agrogeorgia.models.WorkResult;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by Taras on 09.04.2016.
 */
public class LogWorksFragment extends Fragment implements SearchView.OnQueryTextListener{

    Context context;
    View mRootView;

    String[] hours = {"2", "4", "6", "8", "10", "12", "14", "16"};
    String fragmentQuery;

    private Spinner hoursSpinner, worksSpinner;
    private EditText quantity_EditText;
    CheckBox check;

    SearchView search;

    static ArrayList<EmployeeCheck> mEmployeeCheckArrayList;


    ArrayList<WorkResult> mWorkArrayList;

    static List<ComplexWork> mComplexWorkArrayList;
    private ArrayList<ComplexWork> mFilteredEmployeeArrayList;

    Work currentWork;
    String currentHours;
    String currentQuantityHours;

    CharSequence[] choice;
    Project currentProject;
    FixedAssets currentFixedAssets;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppCompatButton postLogWorkButton;

    public ComplexWorkAdapter complexWorkAdapter;

    Context mContext;

    SharedPreferences prefs;
    LocalCredentialStore localCredentialStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        localCredentialStore = new LocalCredentialStore(prefs);

        LoadProjectsAsync loadProjectsAsync = new LoadProjectsAsync();
        loadProjectsAsync.execute();

        mRootView = inflater.inflate(R.layout.logworks_fragment, container, false);

        return mRootView;
    }

    public void initAdapter(){
        //mComplexWorkArrayList = new ArrayList<>();
        complexWorkAdapter = new ComplexWorkAdapter( mComplexWorkArrayList, getActivity() );
        mRecyclerView.setAdapter(complexWorkAdapter);
        complexWorkAdapter.notifyDataSetChanged();
    }

    public void updateRecyclerView(){

        mComplexWorkArrayList = ComplexWork.findWithQuery(ComplexWork.class, "Select * from Complex_work where finished=0 and Work=? and Project=? and fixed_Assets=?", currentWork.getId().toString(), currentProject.getId().toString(),currentFixedAssets.getId().toString());

        if (complexWorkAdapter==null)initAdapter();

        mFilteredEmployeeArrayList = new ArrayList<>();
        mFilteredEmployeeArrayList.addAll(mComplexWorkArrayList);



        complexWorkAdapter.setComplexWorks( mComplexWorkArrayList);
        complexWorkAdapter.notifyDataSetChanged();

        //search.clearFocus();

        updateSearch(fragmentQuery);

    }

    public void setSelectAll(Boolean selected){

        if (complexWorkAdapter==null)initAdapter();

        for (ComplexWork complexWork:mComplexWorkArrayList)
            complexWork.setFinished(selected);

        complexWorkAdapter.setComplexWorks( mComplexWorkArrayList);
        complexWorkAdapter.notifyDataSetChanged();

    };

    public void postWorks(){

        if ((currentWork.getType().equals("1")||currentWork.getType().equals("0"))&&(currentQuantityHours==null||currentQuantityHours.equals("")||(!validateHours()))) {
            Toast.makeText(getActivity(), "Wrong hour values", Toast.LENGTH_SHORT).show();
            return;
        }

        PostLogWorksAsync postLogWorksAsync = new PostLogWorksAsync();
        postLogWorksAsync.execute();
    }

    public boolean validateHours(){
        boolean result = true;
        try{
            Double hours = Double.valueOf(currentQuantityHours);
        }catch (Exception e){
            result = false;
        }
        return result;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        updateSearch(newText);
        return true;
    }

    private List<ComplexWork> filter(List<ComplexWork> currentComplexWorkList, String query) {
        query = query.toLowerCase();
        fragmentQuery = query;

        final List<ComplexWork> filteredModelList = new ArrayList<>();
        for (ComplexWork complexWork : currentComplexWorkList) {
            final String text = complexWork.getEmployee().getEmployeeName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(complexWork);
            }
        }
        return filteredModelList;

    }

    public void updateSearch(String newText){
        final List<ComplexWork> filteredList = filter(mFilteredEmployeeArrayList, newText);
        complexWorkAdapter.animateTo(filteredList);
        mRecyclerView.scrollToPosition(0);
    }

    class PostLogWorksAsync extends AsyncTask<Void, Void, Boolean>{




        @Override
        protected Boolean doInBackground(Void... params) {


                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                for (ComplexWork complexWork:mComplexWorkArrayList){
                    if (!complexWork.getFinished()) continue;

                try {

                    PropertyInfo projectPropertyInfo = new PropertyInfo();
                    projectPropertyInfo.name = "ProjectID";
                    projectPropertyInfo.type = String.class;
                    projectPropertyInfo.setValue(complexWork.getProject().getProjectID());

                    PropertyInfo workPropertyInfo = new PropertyInfo();
                    workPropertyInfo.name = "WorkID";
                    workPropertyInfo.type = String.class;
                    workPropertyInfo.setValue(complexWork.getWork().getWorkId());

                    PropertyInfo employeePropertyInfo = new PropertyInfo();
                    employeePropertyInfo.name = "EmployeeID";
                    employeePropertyInfo.type = String.class;
                    employeePropertyInfo.setValue(complexWork.getEmployee().getEmployeeID());

                    PropertyInfo countPropertyInfo = new PropertyInfo();
                    countPropertyInfo.name = "Count";
                    countPropertyInfo.type = String.class;

                    PropertyInfo fixedAssetsPropertyInfo = new PropertyInfo();
                    fixedAssetsPropertyInfo.name = "FixedAssetsID";
                    fixedAssetsPropertyInfo.type = String.class;
                    fixedAssetsPropertyInfo.setValue(complexWork.getFixedAssets().getFixedAssetsID());

                    if (currentWork.getType().equals("1")||currentWork.getType().equals("0")) {
                        countPropertyInfo.setValue(currentQuantityHours);
                    }else {
                        countPropertyInfo.setValue(currentHours);
                    }

                    SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_POST_WORKS);
                    customer.addProperty(projectPropertyInfo);
                    customer.addProperty(employeePropertyInfo);
                    customer.addProperty(workPropertyInfo);
                    customer.addProperty(countPropertyInfo);
                    customer.addProperty(fixedAssetsPropertyInfo);

                    List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                    headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode( localCredentialStore.getCredentials().getBytes())));

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.encodingStyle = SoapEnvelope.ENC;
                    envelope.setAddAdornments(false);
                    envelope.implicitTypes = false;

                    envelope.setOutputSoapObject(customer);
                    androidHttpTransport.call(localCredentialStore.getCommonUrl() + Constants.POST_WORKS_SOAP_ACTION, envelope, headerList);

                    SoapObject result = (SoapObject) envelope.getResponse();

                    //complexWork.setHours(Double.valueOf(currentHours));

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

                    complexWork.save();

                    //ArrayList<WorkResult> workArrayList = new ArrayList<>();

               /* for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        Work work = new Work();
                        work.setWorkId(final_object.getProperty("WorkID").toString());
                        work.setWorkName(final_object.getProperty("WorkName").toString());

                        WorkResult workResult = new WorkResult();
                        workResult.setWork(work);
                        workResult.setResult(0);
                        //workArrayList.add(workResult);
                    }
                } */



                } catch (Exception ignored) {
                    ignored.printStackTrace();

                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean posted) {
            super.onPostExecute(posted);
            if (posted){
                updateRecyclerView();

                /*Toast.makeText(getActivity().getApplicationContext(), "Posted succesfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();*/
            }else {
                Toast.makeText(getActivity(), "Error while post", Toast.LENGTH_SHORT).show();

            }
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
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode( localCredentialStore.getCredentials().getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call(localCredentialStore.getCommonUrl() + Constants.GET_PROJECTS_SOAP_ACTION, envelope, headerList);

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
            if (projects.size()==0)return;

            mContext = getActivity() ;
            if (mContext!=null)
            showDialog(projects);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class GetWorkPriceAsync extends AsyncTask<String, String, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            try {

                PropertyInfo projectPropertyInfo = new PropertyInfo();
                projectPropertyInfo.name = "ProjectID";
                projectPropertyInfo.type = String.class;
                projectPropertyInfo.setValue(currentProject.getProjectID());

                PropertyInfo workPropertyInfo = new PropertyInfo();
                workPropertyInfo.name = "WorkID";
                workPropertyInfo.type = String.class;
                workPropertyInfo.setValue(currentWork.getWorkId());

                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_GET_WORK_PRICE);
                customer.addProperty(projectPropertyInfo);
                customer.addProperty(workPropertyInfo);

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode( localCredentialStore.getCredentials().getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call(localCredentialStore.getCommonUrl() + Constants.GET_WORK_PRICE, envelope, headerList);

                SoapObject result = (SoapObject) envelope.getResponse();

                for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapPrimitive) {
                       // SoapObject final_object = (SoapObject) property;
                        SoapPrimitive primitive = (SoapPrimitive)property;
                        if (i==0){
                            currentWork.setPrice(primitive.toString());
                            continue;
                        }
                        currentWork.setType(primitive.toString());

                    }
                }

                return true;

            }catch (Exception ignored){
                ignored.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);


            mContext = getActivity() ;
            if (mContext!=null){
                if (currentWork.getType().equals("1")||currentWork.getType().equals("0")){
                    quantity_EditText.setVisibility(View.VISIBLE);
                    hoursSpinner.setVisibility(View.GONE);
                }else {
                    quantity_EditText.setVisibility(View.GONE);
                    hoursSpinner.setVisibility(View.VISIBLE);
                }
            }
                //showDialog(projects);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    public void showDialog(List<Project> listItems){
        currentProject = null;
        List<String> items = new ArrayList<>();
        for (Project project:listItems) items.add(project.getProjectName());

        choice = items.toArray(new CharSequence[items.size()]);



        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(R.string.select_project_name);
        alert.setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentProject = listItems.get(which);
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentProject==null){
                    //Toast.makeText(getActivity(), R.string.select_project_name, Toast.LENGTH_SHORT);
                    return;
                }
                //alert;
                //initControls();
                LoadFixedAssetsAsync loadFixedAssetsAsync = new LoadFixedAssetsAsync();
                loadFixedAssetsAsync.execute();
            }
        });
        alert.setCancelable(true);
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
        alert.show();
    }

    public void showDialogAssets(List<FixedAssets> listItems){
        currentFixedAssets = null;
        List<String> items = new ArrayList<>();
        for (FixedAssets fixedAssets:listItems) items.add(fixedAssets.getFixedAssetsName());

        choice = items.toArray(new CharSequence[items.size()]);

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(R.string.select_fixed_assets_name);
        alert.setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentFixedAssets = listItems.get(which);
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentFixedAssets==null){
                    //Toast.makeText(getActivity(), R.string.select_project_name, Toast.LENGTH_SHORT);
                    return;
                }
                //alert;
                initControls();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    private void initControls() {

        hoursSpinner = (Spinner)mRootView.findViewById(R.id.hoursSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, hours);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursSpinner.setAdapter(adapter);
        hoursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentHours = hours[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        worksSpinner = (Spinner)mRootView.findViewById(R.id.worksSpinner);
        List<Work> works = Work.listAll(Work.class);
        ArrayAdapter<Work> worksAdapter = new ArrayAdapter<Work>(getActivity(), android.R.layout.simple_spinner_dropdown_item, works);
        worksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        worksSpinner.setAdapter(worksAdapter);
        worksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentWork = works.get(position);
                updateRecyclerView();

                GetWorkPriceAsync getWorkPriceAsync = new GetWorkPriceAsync();
                getWorkPriceAsync.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        quantity_EditText = (EditText)mRootView.findViewById(R.id.quantity_EditText);
        quantity_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuantityHours = quantity_EditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.employees_recycler_view);
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        postLogWorkButton = (AppCompatButton) mRootView.findViewById(R.id.btnPostWork);
        postLogWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postWorks();
            }
        });

        //updateRecyclerView();

        mEmployeeCheckArrayList = new ArrayList<>();

        check = (CheckBox) mRootView.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectAll(check.isChecked());
            }
        });

        mRecyclerView.setAdapter(complexWorkAdapter);

        search = (SearchView) mRootView.findViewById(R.id.searchView);
        search.setOnQueryTextListener(this);

        fragmentQuery = "";

    }

    class LoadFixedAssetsAsync extends AsyncTask<String, String, ArrayList<FixedAssets>> {

        private Object readProperty(int index, SoapObject item){
            PropertyInfo p = new PropertyInfo();
            item.getPropertyInfo(index, p);
            return p.getValue();
        }

        @Override
        protected ArrayList<FixedAssets> doInBackground(String... params) {
            try {
                PropertyInfo projectPropertyInfo = new PropertyInfo();
                projectPropertyInfo.name = "ProjectID";
                projectPropertyInfo.type = String.class;
                projectPropertyInfo.setValue(currentProject.getProjectID());

                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_GET_FIXED_ASSETS);
                customer.addProperty(projectPropertyInfo);

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode( localCredentialStore.getCredentials().getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call(localCredentialStore.getCommonUrl() + Constants.GET_FIXED_ASSETS_SOAP_ACTION, envelope, headerList);

                SoapObject result = (SoapObject) envelope.getResponse();

                ArrayList<FixedAssets> fixedAssetsArrayList = new ArrayList<>();

                for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        FixedAssets fixedAssets = new FixedAssets();
                        fixedAssets.setFixedAssetsID(final_object.getProperty("FixedAssetsID").toString());
                        fixedAssets.setFixedAssetsName(final_object.getProperty("FixedAssetsName").toString());

                        List<FixedAssets> findFixedAssets = FixedAssets.findWithQuery(FixedAssets.class, "SELECT * FROM FIXED_ASSETS WHERE FIXED_ASSETS_ID = ?", fixedAssets.getFixedAssetsID());
                        if (findFixedAssets.size()==0) {
                            fixedAssets.save();
                        } else {
                            fixedAssets = findFixedAssets.get(0);
                        }

                        //fixedAssets.save();

                        fixedAssetsArrayList.add(fixedAssets);
                    }
                }

                return fixedAssetsArrayList;

            }catch (Exception ignored){
                ignored.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<FixedAssets> fixedAssets) {
            super.onPostExecute(fixedAssets);
            if (fixedAssets.size()==0){
                Toast toast = Toast.makeText(getContext().getApplicationContext(),
                        R.string.no_fixed_assets, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                LoadProjectsAsync loadProjectsAsync = new LoadProjectsAsync();
                loadProjectsAsync.execute();
                return;
            }

            mContext = getActivity() ;
            if (mContext!=null)
                showDialogAssets(fixedAssets);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
