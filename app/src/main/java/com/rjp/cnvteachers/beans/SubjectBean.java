package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 4/26/2017.
 */

public class SubjectBean implements Serializable{

    private String sub_id,sub_name;

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    @Override
    public String toString() {
        return sub_name;
    }
}
