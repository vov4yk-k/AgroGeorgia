package ua.com.dev_club.agrogeorgia.models;

import com.orm.SugarRecord;

/**
 * Created by Vova on 06.05.2017.
 */

public class FixedAssets extends SugarRecord {
    private String FixedAssetsID;
    private String FixedAssetsName;

    public FixedAssets(){
    }
    public FixedAssets(String fixedAssetsID, String fixedAssetsName) {
        FixedAssetsID = fixedAssetsID;
        FixedAssetsName = fixedAssetsName;
    }

    public String getFixedAssetsID() {
        return FixedAssetsID;
    }

    public String getFixedAssetsName() {
        return FixedAssetsName;
    }

    public void setFixedAssetsID(String fixedAssetsID) {
        FixedAssetsID = fixedAssetsID;
    }

    public void setFixedAssetsName(String fixedAssetsName) {
        FixedAssetsName = fixedAssetsName;
    }
}
