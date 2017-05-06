package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 4/26/2017.
 */

public class RemarkBean implements Serializable {
    private String Remark,Remark_id, Value;

    @Override
    public String toString() {
        return Remark;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getRemark_id() {
        return Remark_id;
    }

    public void setRemark_id(String remark_id) {
        Remark_id = remark_id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        this.Value = value;
    }
}
