package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 4/5/2017.
 */

public class AcademicYearBean implements Serializable{

String acad_year;

    @Override
    public String toString() {
        return acad_year;
    }

    public String getAcad_year() {
        return acad_year;
    }

    public void setAcad_year(String acad_year) {
        this.acad_year = acad_year;
    }
}
