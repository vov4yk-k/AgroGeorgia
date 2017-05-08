package ua.com.dev_club.agrogeorgia.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
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
import ua.com.dev_club.agrogeorgia.activities.MainActivity;
import ua.com.dev_club.agrogeorgia.activities.WorksActivity;
import ua.com.dev_club.agrogeorgia.adapters.EmployeesAdapter;
import ua.com.dev_club.agrogeorgia.adapters.WorksAdapter;
import ua.com.dev_club.agrogeorgia.api.Constants;
import ua.com.dev_club.agrogeorgia.helpers.LogWorkHelper;
import ua.com.dev_club.agrogeorgia.models.ComplexWork;
import ua.com.dev_club.agrogeorgia.models.Employee;
import ua.com.dev_club.agrogeorgia.models.EmployeeCheck;
import ua.com.dev_club.agrogeorgia.models.FixedAssets;
import ua.com.dev_club.agrogeorgia.models.Project;
import ua.com.dev_club.agrogeorgia.models.Work;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by Taras on 09.04.2016.
 */
public class StartWorksFragment extends Fragment implements SearchView.OnQueryTextListener{

    Context context;
    View mRootView;

    CharSequence[] choice;
    Project currentProject;
    Work currentWork;
    FixedAssets currentFixedAssets;

    private static ArrayList<Work> mWorkArrayList;
    private static ArrayList<EmployeeCheck> mEmployeeArrayList;
    private ArrayList<EmployeeCheck> mFilteredEmployeeArrayList;

    private RecyclerView employeeChooseRecyclerView;
    private EmployeesAdapter employeesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    Button startWorkButton;
    Spinner spinner;

    SharedPreferences prefs;
    LocalCredentialStore localCredentialStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        localCredentialStore = new LocalCredentialStore(prefs);

        LoadProjectsAsync loadProjectsAsync = new LoadProjectsAsync();
        loadProjectsAsync.execute();

        mRootView = inflater.inflate(R.layout.new_start_work, container, false);


        return mRootView;
    }

    public void initControls(){

        startWorkButton = (Button) mRootView.findViewById(R.id.startWorkButton);
        startWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWork==null)return;
                if (currentProject==null)return;
                if (currentFixedAssets==null) return;

                for (EmployeeCheck employeeCheck:mEmployeeArrayList){
                    if (employeeCheck.getChecked()){
                        ComplexWork complexWork = new ComplexWork();
                        complexWork.setProject(currentProject);
                        complexWork.setWork(currentWork);
                        complexWork.setFixedAssets(currentFixedAssets);
                        complexWork.setEmployee(employeeCheck.getEmployee());
                        complexWork.setHours(0d);
                        complexWork.setFinished(false);
                        complexWork.save();
                    }
                }

                Toast.makeText(getContext(), R.string.work_has_been_started, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        spinner = (Spinner)mRootView.findViewById(R.id.workSpinner);

        employeeChooseRecyclerView = (RecyclerView) mRootView.findViewById(R.id.employeeChooseRecyclerView);
        employeeChooseRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        employeeChooseRecyclerView.setLayoutManager(mLayoutManager);

        mEmployeeArrayList = new ArrayList<>();

        LoadWorksAsync loadWorksAsync = new LoadWorksAsync();
        loadWorksAsync.execute();

        LoadEmployeeAsync loadEmployeeAsync = new LoadEmployeeAsync();
        loadEmployeeAsync.execute();

        CheckBox check = (CheckBox) mRootView.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectAll(check.isChecked());
            }
        });

        SearchView search = (SearchView) mRootView.findViewById(R.id.searchView);
        search.setOnQueryTextListener(this);

    }

    public void setSelectAll(Boolean selected){
        for (EmployeeCheck employeeCheck:mEmployeeArrayList)
            employeeCheck.setChecked(selected);
        employeesAdapter.setEmployeeArrayList(mEmployeeArrayList);
        employeesAdapter.notifyDataSetChanged();
    };

    public void showDialog(List<Project> listItems){

        List<String> items = new ArrayList<>();
        for (Project project:listItems) items.add(project.getProjectName());

        choice = items.toArray(new CharSequence[items.size()]);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);
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
                //alert;
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

        List<String> items = new ArrayList<>();
        for (FixedAssets fixedAssets:listItems) items.add(fixedAssets.getFixedAssetsName());

        choice = items.toArray(new CharSequence[items.size()]);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);
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
                initControls();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<EmployeeCheck> filteredList = filter(mFilteredEmployeeArrayList, newText);
        employeesAdapter.animateTo(filteredList);
        employeeChooseRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<EmployeeCheck> filter(List<EmployeeCheck> currentEmployeeCheckList, String query) {
        query = query.toLowerCase();

        final List<EmployeeCheck> filteredModelList = new ArrayList<>();
        for (EmployeeCheck employeeCheck : currentEmployeeCheckList) {
            final String text = employeeCheck.getEmployee().getEmployeeName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(employeeCheck);
            }
        }
        return filteredModelList;

    }

    class LoadWorksAsync extends AsyncTask<String, String, ArrayList<Work>> {

        private Object readProperty(int index, SoapObject item){
            PropertyInfo p = new PropertyInfo();
            item.getPropertyInfo(index, p);
            return p.getValue();
        }

        @Override
        protected ArrayList<Work> doInBackground(String... params) {
            try {

                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_GET_WORKS);

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode( localCredentialStore.getCredentials().getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call(localCredentialStore.getCommonUrl() + Constants.GET_WORKS_SOAP_ACTION, envelope, headerList);

                SoapObject result = (SoapObject) envelope.getResponse();

                ArrayList<Work> workArrayList = new ArrayList<>();

                for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        Work work = new Work();
                        work.setWorkId(final_object.getProperty("WorkID").toString());
                        work.setWorkName(final_object.getProperty("WorkName").toString());

                       // List<Work> findWork = Work.find(Work.class, "workId = ?", work.getWorkId());
                        List<Work> findWork = Work.findWithQuery(Work.class, "Select * from Work where work_id = ?", work.getWorkId());
                        if (findWork.size()==0) {
                            work.save();

                        } else {
                            work = findWork.get(0);
                        }

                        workArrayList.add(work);
                    }
                }

                return workArrayList;

            }catch (Exception ignored){
                ignored.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<Work> works) {
            super.onPostExecute(works);

            mWorkArrayList = works;

            if (mWorkArrayList==null) return;

            ArrayAdapter<Work> adapter = new ArrayAdapter<Work>(getActivity(), R.layout.spinner_item, mWorkArrayList);

            adapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentWork = mWorkArrayList.get(position);

                    /*switch (position) {
                        default:
                           // LogWorkHelper.getLogWork().setEmployee(mWorkArrayList.get(position));
                            break;
                    }*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if (mWorkArrayList!=null && mWorkArrayList.size()>0)
                    currentWork = mWorkArrayList.get(0);
                }
            });

            //currentWork =


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        /*    if(isAdded()){
                if(!refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(true);
            }*/
        }
    }

    class LoadEmployeeAsync extends AsyncTask<String, String, ArrayList<Employee>> {

        private Object readProperty(int index, SoapObject item){
            PropertyInfo p = new PropertyInfo();
            item.getPropertyInfo(index, p);
            return p.getValue();
        }

        @Override
        protected ArrayList<Employee> doInBackground(String... params) {
            try {

                HttpTransportSE androidHttpTransport = new HttpTransportSE(localCredentialStore.getCommonUrl() + Constants.URL);

                SoapObject customer = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_GET_EMPLOYEES);

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode( localCredentialStore.getCredentials().getBytes())));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setAddAdornments(false);
                envelope.implicitTypes = false;

                envelope.setOutputSoapObject(customer);
                androidHttpTransport.call(localCredentialStore.getCommonUrl() + Constants.GET_EMPLOYEES_SOAP_ACTION, envelope, headerList);

                SoapObject result = (SoapObject) envelope.getResponse();

                ArrayList<Employee> employeeArrayList = new ArrayList<>();

                for(int i=0;i<result.getPropertyCount();i++) {
                    Object property = result.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        Employee employee = new Employee();
                        employee.setEmployeeID(final_object.getProperty("EmployeeID").toString());
                        employee.setEmployeeName(final_object.getProperty("EmployeeName").toString());

                        List<Employee> findEmployees = Work.findWithQuery(Employee.class, "Select * from Employee where employee_id = ?", employee.getEmployeeID());
                        if (findEmployees.size()==0) {
                            employee.save();

                        } else {
                            employee = findEmployees.get(0);
                        }
                        employee.save();
                        employeeArrayList.add(employee);
                    }
                }

                return employeeArrayList;

            }catch (Exception ignored){
                ignored.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<Employee> employees) {
            super.onPostExecute(employees);
            if (employees.size()==0)return;

            for (Employee employee:employees)
            mEmployeeArrayList.add(new EmployeeCheck(employee, false));

            if (mEmployeeArrayList==null)return;

            mFilteredEmployeeArrayList = new ArrayList<>();
            mFilteredEmployeeArrayList.addAll(mEmployeeArrayList);

            employeesAdapter = new EmployeesAdapter(mEmployeeArrayList, getActivity());
            employeeChooseRecyclerView.setAdapter(employeesAdapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            Context mContext = getActivity();
            if (mContext!=null)
            showDialog(projects);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
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

                ArrayList<FixedAssets> fixeddAssetsArrayList = new ArrayList<>();

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

                        fixeddAssetsArrayList.add(fixedAssets);
                    }
                }

                return fixeddAssetsArrayList;

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

            Context mContext = getActivity();
            if (mContext!=null)
                showDialogAssets(fixedAssets);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
