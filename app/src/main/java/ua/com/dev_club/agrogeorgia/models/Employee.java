package ua.com.dev_club.agrogeorgia.models;

import com.orm.SugarRecord;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Taras on 05.04.2016.
 */
@Root(name="Employee")
public class Employee extends SugarRecord{
    @Element(name="EmployeeID")
    private String EmployeeID;
    @Element(name="EmployeeName")
    private String EmployeeName;

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    @Override
    public String toString() {
        return getEmployeeName();
    }

    @Override
    public boolean equals(Object o) {
        return getEmployeeID().equals(((Employee)o).getEmployeeID());
    }
}
