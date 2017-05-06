package ua.com.dev_club.agrogeorgia.models;

/**
 * Created by Taras on 10.04.2016.
 */
public class WorkResult {
    Work work;
    Integer result;

    public WorkResult() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
