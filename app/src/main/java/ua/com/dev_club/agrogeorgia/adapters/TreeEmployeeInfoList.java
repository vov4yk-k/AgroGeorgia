package ua.com.dev_club.agrogeorgia.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import ua.com.dev_club.agrogeorgia.models.Employee;

/**
 * Created by Vova on 10.05.2017.
 */
/**
 *
 * first level item
 *
 */
public class TreeEmployeeInfoList {

    private String projectName;
    private ArrayList<FixedAssetLevel> fixedAssetList;

    public TreeEmployeeInfoList(String projectName, ArrayList<FixedAssetLevel> fixedAssetList){
        super();
        this.projectName = projectName;
        this.fixedAssetList = fixedAssetList;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ArrayList<FixedAssetLevel> getFixedAssetList() {
        return fixedAssetList;
    }

    public void setFixedAssetList(ArrayList<FixedAssetLevel> fixedAssetList) {
        this.fixedAssetList = fixedAssetList;
    }

    /**
     *
     * second level item
     *
     */

    public static class FixedAssetLevel{

        private String fixedAssetName;
        private ArrayList<WorkLevel> workLevelList;

        public FixedAssetLevel(String fixedAssetName, ArrayList<WorkLevel> workLevelList) {
            super();
            this.fixedAssetName = fixedAssetName;
            this.workLevelList = workLevelList;
        }

        public String getFixedAssetName() {
            return fixedAssetName;
        }

        public void setFixedAssetName(String fixedAssetName) {
            this.fixedAssetName = fixedAssetName;
        }

        public ArrayList<WorkLevel> getWorkLevelList() {
            return workLevelList;
        }

        public void setWorkLevelList(ArrayList<WorkLevel> workLevelList) {
            this.workLevelList = workLevelList;
        }

        /**
         *
         * third level item
         *
         */
        public static class WorkLevel {
            private String workName;
            private ArrayList<EmployeeLevel> employeeLevelList;

            public WorkLevel(String workName, ArrayList<EmployeeLevel> employeeLevelList) {
                super();
                this.workName = workName;
                setGroupEmployeeLevelList(employeeLevelList);
            }

            public String getWorkName() {
                return workName;
            }

            public void setWorkName(String workName) {
                this.workName = workName;
            }

            public ArrayList<EmployeeLevel> getEmployeeLevelList() {
                return employeeLevelList;
            }

            public void setEmployeeLevelList(ArrayList<EmployeeLevel> employeeLevelList) {
                setGroupEmployeeLevelList(employeeLevelList);
            }

            private void setGroupEmployeeLevelList(ArrayList<EmployeeLevel> employeeLevelList){
                HashSet<EmployeeLevel> employeeSet = new HashSet(employeeLevelList);
                ArrayList<EmployeeLevel> newEmployeeLevelList = new ArrayList<EmployeeLevel>();
                for (EmployeeLevel currEmployeeLevel:employeeSet){
                    Double hours = 0.0;
                    for (EmployeeLevel el:employeeLevelList){
                       if(el.getEmployeeID().equals(currEmployeeLevel.getEmployeeID())) hours+=el.getHours();
                    }
                    newEmployeeLevelList.add(new EmployeeLevel(currEmployeeLevel.getEmployeeName(),hours,currEmployeeLevel.getEmployeeID()));
                }
                this.employeeLevelList = newEmployeeLevelList;
            }
            /**
             * fourht level item
             */
            public static class EmployeeLevel {

                private String employeeName;
                private Double hours;
                private String employeeID;

                public EmployeeLevel(String employeeName, Double hours, String employeeID) {
                    this.employeeName = employeeName;
                    this.hours = hours;
                    this.employeeID = employeeID;
                }

                public String getEmployeeName() {
                    return employeeName;
                }

                public void setEmployeeName(String employeeName) {
                    this.employeeName = employeeName;
                }

                public Double getHours() {
                    return hours;
                }

                public void setHours(Double hours) {
                    this.hours = hours;
                }

                public String getEmployeeID() {
                    return employeeID;
                }

                public void setEmployeeID(String employeeID) {
                    this.employeeID = employeeID;
                }

                @Override
                public boolean equals(Object o) {
                    if(o==null) return false;
                    EmployeeLevel employeeLevelObject = (EmployeeLevel) o;
                    return employeeID.equals(employeeLevelObject.getEmployeeID());
                }

                @Override
                public int hashCode() {
                    return employeeID.hashCode();
                }
            }
        }
    }
}
