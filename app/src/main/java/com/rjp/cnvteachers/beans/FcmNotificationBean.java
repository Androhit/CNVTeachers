package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class FcmNotificationBean implements Serializable{

    private String chrDocNo,sender_id,receiver_id,media_url;
    private int sender_type,receiver_type,msg_type,title_id,sent_by;
    private String message,title,entry_date;
    private String sender_name,receiver_name,app_name;

    public FcmNotificationBean()
    {
        sent_by = 0;
    }

    public int getSent_by() {
        return sent_by;
    }

    public void setSent_by(int sent_by) {
        this.sent_by = sent_by;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getChrDocNo() {
        return chrDocNo;
    }

    public void setChrDocNo(String chrDocNo) {
        this.chrDocNo = chrDocNo;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public int getSender_type() {
        return sender_type;
    }

    public void setSender_type(int sender_type) {
        this.sender_type = sender_type;
    }

    public int getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(int receiver_type) {
        this.receiver_type = receiver_type;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public int getTitle_id() {
        return title_id;
    }

    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }
}
