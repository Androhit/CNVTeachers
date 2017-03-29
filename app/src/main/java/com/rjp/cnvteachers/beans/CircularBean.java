package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 3/27/2017.
 */

public class CircularBean implements Serializable{
    private String cir_id,cir_date,circular_title,time_created,date_created,msg_text,id,createdby,attachment;

    public String getCir_id() {
        return cir_id;
    }

    public void setCir_id(String cir_id) {
        this.cir_id = cir_id;
    }

    public String getCir_date() {
        return cir_date;
    }

    public void setCir_date(String cir_date) {
        this.cir_date = cir_date;
    }

    public String getCircular_title() {
        return circular_title;
    }

    public void setCircular_title(String circular_title) {
        this.circular_title = circular_title;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getMsg_text() {
        return msg_text;
    }

    public void setMsg_text(String msg_text) {
        this.msg_text = msg_text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

}
