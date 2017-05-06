package ua.com.dev_club.agrogeorgia.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import ua.com.dev_club.agrogeorgia.R;
import ua.com.dev_club.agrogeorgia.adapters.EmployeesAdapter;
import ua.com.dev_club.agrogeorgia.api.Constants;
import ua.com.dev_club.agrogeorgia.models.Employee;
import ua.com.dev_club.agrogeorgia.utils.LocalCredentialStore;

/**
 * Created by Taras on 09.04.2016.
 */
public class EmployeeFragment extends Fragment{

    Context context;
    View mRootView;

    ArrayList<Employee> mEmployeeArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences prefs;
    LocalCredentialStore localCredentialStore;

    SearchView search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        localCredentialStore = new LocalCredentialStore(prefs);

        mRootView = inflater.inflate(R.layout.employee_fragment, container, false);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.goods_recycler_view);
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        LoadEmployeeAsync loadEmployeeAsync = new LoadEmployeeAsync();
        loadEmployeeAsync.execute();



        return mRootView;
    }


    class LoadEmployeeAsync extends AsyncTask<String, String, ArrayList<Employee>> {

        private Object readProperty(int index, SoapObject item){
            PropertyInfo p = new PropertyInfo();
            item.getPropertyInfo(index, p);
            return p.getValue();
        }

        private ArrayList<Employee> onReadChildren(SoapObject employee){
            if(employee.hasProperty("children")) {
                ArrayList<Employee> childrenArray = new ArrayList<>();
                PropertyInfo foo = new PropertyInfo();
                employee.getPropertyInfo(6, foo);
                SoapObject childrens = (SoapObject)foo.getValue();
                int count = childrens.getPropertyCount();
                for (int i = 0; i < count; i++) {
                    PropertyInfo p = new PropertyInfo();
                    childrens.getPropertyInfo(i, p);
                    SoapObject item = (SoapObject)p.getValue();
                    Employee cat = new Employee();
                    cat.setEmployeeID((String) (readProperty(0, item)));
                    cat.setEmployeeName((String) (readProperty(1, item)));

                    SoapObject cc = (SoapObject)readProperty(6, item);
                    childrenArray.add(cat);
                }
                return childrenArray;
            }
            return new ArrayList<>();
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

            /*mAdapter = new EmployeesAdapter(employees, getActivity());
            mRecyclerView.setAdapter(mAdapter);*/


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

}
