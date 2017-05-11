package ua.com.dev_club.agrogeorgia.models;

import com.orm.SugarRecord;

/**
 * Created by 1cspe on 25.04.2016.
 */
public class Project extends SugarRecord{
    private String ProjectID;
    private String ProjectName;

    public Project() {
    }

    public Project(String projectID, String projectName) {
        ProjectID = projectID;
        ProjectName = projectName;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    @Override
    public boolean equals(Object o) {
        if(o==null) return false;
        Project projecttObject = (Project) o;
        return ProjectID.equals(projecttObject.getProjectID());
    }

    @Override
    public int hashCode() {
        return getProjectID().hashCode();
    }
}
