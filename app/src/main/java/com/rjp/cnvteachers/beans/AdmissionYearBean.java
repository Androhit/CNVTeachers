package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 4/5/2017.
 */

public class AdmissionYearBean implements Serializable {
    String adm_yr;

    @Override
    public String toString() {
        return adm_yr;
    }

    public String getAdm_yr() {
        return adm_yr;
    }

    public void setAdm_yr(String adm_yr) {
        this.adm_yr = adm_yr;
    }
}
