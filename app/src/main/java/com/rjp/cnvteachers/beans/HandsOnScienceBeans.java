package com.rjp.cnvteachers.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shraddha on 3/29/2017.
 */
public class HandsOnScienceBeans implements Serializable{

    private String id,proj_aim,proj_title,proj_disc,proj_id,from_date,to_date,guided_by,attachment;
    private ArrayList<String> count;

    public ArrayList<String> getCount() {
        return count;
    }

    public void setCount(ArrayList<String> count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProj_aim() {
        return proj_aim;
    }

    public void setProj_aim(String proj_aim) {
        this.proj_aim = proj_aim;
    }

    public String getProj_title() {
        return proj_title;
    }

    public void setProj_title(String proj_title) {
        this.proj_title = proj_title;
    }

    public String getProj_disc() {
        return proj_disc;
    }

    public void setProj_disc(String proj_disc) {
        this.proj_disc = proj_disc;
    }

    public String getProj_id() {
        return proj_id;
    }

    public void setProj_id(String proj_id) {
        this.proj_id = proj_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getGuided_by() {
        return guided_by;
    }

    public void setGuided_by(String guided_by) {
        this.guided_by = guided_by;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
