package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 3/11/2017.
 */

public class DivisonBean implements Serializable {
    private String division_name,div_id;

    @Override
    public String toString() {
        return division_name;
    }

    public void setDivision_name(String division_name) {
        this.division_name = division_name;
    }

    public String getDivision_name() {
        return division_name;
    }


    public String getDiv_id() {
        return div_id;
    }

    public void setDiv_id(String div_id) {
        this.div_id = div_id;
    }
}
