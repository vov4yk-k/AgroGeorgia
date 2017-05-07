package ua.com.dev_club.agrogeorgia.api;

/**
 * Created by Taras on 05.04.2016.
 */
public class Constants {

    // public static String HOSTNAME = "http://srj-erp.gate.ge/";

    public static String HOSTNAME = "";

    public final static String URL = HOSTNAME + "ws/DirectOneWorkLog.1cws";

    public final static String NAMESPACE = "http://directone.ge/DirectOneWorkLog";

    public final static String GET_EMPLOYEES_SOAP_ACTION = HOSTNAME + "ws/DirectOneWorkLog.1cws/GetEmployees";

    public final static String METHOD_NAME_GET_EMPLOYEES = "GetEmployees";


    // Works
    public final static String GET_WORKS_SOAP_ACTION = HOSTNAME + "ws/DirectOneWorkLog.1cws/GetWorks";

    public final static String METHOD_NAME_GET_WORKS = "GetWorks";

    // Users
    public final static String GET_USERS_SOAP_ACTION = HOSTNAME + "ws/DirectOneWorkLog.1cws/GetUsers";

    public final static String METHOD_NAME_GET_USERS = "GetUsers";

    // Projects
    public final static String GET_PROJECTS_SOAP_ACTION = HOSTNAME + "ws/DirectOneWorkLog.1cws/GetProjects";

    public final static String METHOD_NAME_GET_PROJECTS = "GetProjects";

    // Work price
    public final static String GET_WORK_PRICE = HOSTNAME + "ws/DirectOneWorkLog.1cws/GetWorkPrice";

    public final static String METHOD_NAME_GET_WORK_PRICE = "GetWorkPrice";


    // LogWork
    public final static String POST_WORKS_SOAP_ACTION = HOSTNAME + "ws/DirectOneWorkLog.1cws/LogWork";

    public final static String METHOD_NAME_POST_WORKS = "LogWork";

    // FixedAssets
    public final static String GET_FIXED_ASSETS_SOAP_ACTION = HOSTNAME + "ws/DirectOneWorkLog.1cws/GetFixedAssets";

    public final static String METHOD_NAME_GET_FIXED_ASSETS = "GetFixedAssets";

}
