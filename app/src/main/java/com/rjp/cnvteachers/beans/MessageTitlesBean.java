package com.rjp.cnvteachers.beans;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class MessageTitlesBean{

    private String chrDocNo,title,desc;

    @Override
    public String toString()
    {
        return title;
    }

    public String getChrDocNo() {
        return chrDocNo;
    }

    public void setChrDocNo(String chrDocNo) {
        this.chrDocNo = chrDocNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
