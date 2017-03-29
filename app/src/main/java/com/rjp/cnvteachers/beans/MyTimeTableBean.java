package com.rjp.cnvteachers.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shraddha on 3/20/2017.
 */

public class MyTimeTableBean implements Serializable {


    private String day,subject_short,subject_full,class_id,class_name,roomno,period,subject_id,fromtime,totime,total_hr,lec_type,division;

    private ArrayList<MyTimeTableBean> mon;
    private ArrayList<MyTimeTableBean> tue;
    private ArrayList<MyTimeTableBean> wed;
    private ArrayList<MyTimeTableBean> thu;
    private ArrayList<MyTimeTableBean> fri;
    private ArrayList<MyTimeTableBean> sat;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getLec_type() {
        return lec_type;
    }

    public void setLec_type(String lec_type) {
        this.lec_type = lec_type;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public ArrayList<MyTimeTableBean> getMon() {
        return mon;
    }

    public void setMon(ArrayList<MyTimeTableBean> mon) {
        this.mon = mon;
    }

    public ArrayList<MyTimeTableBean> getTue() {
        return tue;
    }

    public void setTue(ArrayList<MyTimeTableBean> tue) {
        this.tue = tue;
    }

    public ArrayList<MyTimeTableBean> getWed() {
        return wed;
    }

    public void setWed(ArrayList<MyTimeTableBean> wed) {
        this.wed = wed;
    }

    public ArrayList<MyTimeTableBean> getThu() {
        return thu;
    }

    public void setThu(ArrayList<MyTimeTableBean> thu) {
        this.thu = thu;
    }

    public ArrayList<MyTimeTableBean> getFri() {
        return fri;
    }

    public void setFri(ArrayList<MyTimeTableBean> fri) {
        this.fri = fri;
    }

    public ArrayList<MyTimeTableBean> getSat() {
        return sat;
    }

    public void setSat(ArrayList<MyTimeTableBean> sat) {
        this.sat = sat;
    }

    public String getSubject_short() {
        return subject_short;
    }

    public void setSubject_short(String subject_short) {
        this.subject_short = subject_short;
    }

    public String getSubject_full() {
        return subject_full;
    }

    public void setSubject_full(String subject_full) {
        this.subject_full = subject_full;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getTotal_hr() {
        return total_hr;
    }

    public void setTotal_hr(String total_hr) {
        this.total_hr = total_hr;
    }
}
