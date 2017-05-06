package ua.com.dev_club.agrogeorgia.models;

/**
 * Created by 1cspe on 25.04.2016.
 */
public class EmployeeCheck {
    Employee employee;
    Boolean checked;

    public EmployeeCheck(Employee employee) {
        this.employee = employee;
    }

    public EmployeeCheck(Employee employee, Boolean checked) {
        this.employee = employee;
        this.checked = checked;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
