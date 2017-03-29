package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 3/29/2017.
 */

public class AdmissionBean implements Serializable {

    String admno;

    @Override
    public String toString() {
        return admno;
    }


    public String getAdmno() {
        return admno;
    }

    public void setAdmno(String admno) {
        this.admno = admno;
    }
}
