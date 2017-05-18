package ua.com.dev_club.agrogeorgia.models;

/**
 * Created by 1cspe on 04.07.2016.
 */
public class User {
    String userID;
    String userName;
    Boolean userIsAdmin;
    Boolean creatingEmployee;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getUserIsAdmin() {
        return userIsAdmin;
    }

    public void setUserIsAdmin(Boolean userIsAdmin) {
        this.userIsAdmin = userIsAdmin;
    }

    public Boolean getCreatingEmployee() {
        return creatingEmployee;
    }

    public void setCreatingEmployee(Boolean creatingEmployee) {
        this.creatingEmployee = creatingEmployee;
    }
}
