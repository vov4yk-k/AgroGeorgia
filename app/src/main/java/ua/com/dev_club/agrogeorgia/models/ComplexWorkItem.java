package ua.com.dev_club.agrogeorgia.models;

/**
 * Created by Taras on 26.04.2016.
 */
public class ComplexWorkItem implements Cloneable{
    ComplexWork complexWork;
    Double totalHours;
    Boolean isEmployeeItem = false;


    public ComplexWorkItem() {
        isEmployeeItem = false;
    }

    public ComplexWorkItem(ComplexWork complexWork, Double totalHours) {
        this.complexWork = complexWork;
        this.totalHours = totalHours;
    }

    public ComplexWorkItem(ComplexWork complexWork, Double totalHours, Boolean isEmployeeItem) {
        this.complexWork = complexWork;
        this.totalHours = totalHours;
        this.isEmployeeItem = isEmployeeItem;
    }

    public void add(ComplexWorkItem complexWork)    {
        this.totalHours = this.totalHours + complexWork.getTotalHours();
    }

    public void subtract(ComplexWorkItem otherItem)    {
        this.totalHours = this.totalHours - otherItem.getTotalHours();
    }

    public boolean equals(Object other) {
        if (isEmployeeItem) return complexWork.getEmployee().equals(((ComplexWorkItem) other).getComplexWork().getEmployee());
        return complexWork.getDate().equals(((ComplexWorkItem) other).getComplexWork().getDate());
    }

    public ComplexWork getComplexWork() {
        return complexWork;
    }

    public void setComplexWork(ComplexWork complexWork) {
        this.complexWork = complexWork;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Boolean getEmployeeItem() {
        return isEmployeeItem;
    }

    public void setEmployeeItem(Boolean employeeItem) {
        isEmployeeItem = employeeItem;
    }
}
