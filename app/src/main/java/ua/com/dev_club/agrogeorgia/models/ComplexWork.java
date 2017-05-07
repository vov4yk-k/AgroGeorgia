package ua.com.dev_club.agrogeorgia.models;

import com.orm.SugarRecord;

/**
 * Created by Taras on 26.04.2016.
 */
public class ComplexWork extends SugarRecord {
    Project project;
    FixedAssets fixedAssets;
    Work work;
    Employee employee;
    String date;
    Double hours;
    Boolean finished;
    Integer year;
    Integer month;
    Integer day;

    public ComplexWork() {
    }

    public ComplexWork(String date, Employee employee, Boolean finished, Double hours, Project project, Work work, FixedAssets fixedAssets) {
        this.date = date;
        this.employee = employee;
        this.finished = finished;
        this.hours = hours;
        this.project = project;
        this.work = work;
        this.fixedAssets = fixedAssets;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Work getWork() {
        return work;
    }



    public void setWork(Work work) {
        this.work = work;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public FixedAssets getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(FixedAssets fixedAssets) {
        this.fixedAssets = fixedAssets;
    }
}
