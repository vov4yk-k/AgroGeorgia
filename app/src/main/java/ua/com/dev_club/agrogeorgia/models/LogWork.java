package ua.com.dev_club.agrogeorgia.models;

import java.util.List;

/**
 * Created by Taras on 10.04.2016.
 */
public final class LogWork {
    Employee employee;
    List<WorkResult> workList;

    public LogWork() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<WorkResult> getWorkList() {
        return workList;
    }

    public void setWorkList(List<WorkResult> workList) {
        this.workList = workList;
    }
}
