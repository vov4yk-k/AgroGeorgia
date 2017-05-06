package ua.com.dev_club.agrogeorgia.models;

/**
 * Created by 1cspe on 28.04.2016.
 */
public class DayStatistic {
    int day;
    ComplexWorkItem complexWorkItem;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ComplexWorkItem getComplexWorkItem() {
        return complexWorkItem;
    }

    public void setComplexWorkItem(ComplexWorkItem complexWorkItem) {
        this.complexWorkItem = complexWorkItem;
    }
}
