package ua.com.dev_club.agrogeorgia.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Taras on 10.04.2016.
 */
public class Work extends SugarRecord{
    private String workId;
    private String workName;
    @Ignore
    private String type;
    @Ignore
    private String price;

    public Work() {
        setPrice("");
        setType("1");
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return getWorkName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Work work = (Work) o;

        if (!workId.equals(work.workId)) return false;
        return workName.equals(work.workName);

    }

    @Override
    public int hashCode() {
        int result = workId.hashCode();
        result = 31 * result + workName.hashCode();
        return result;
    }
}
