package com.rjp.cnvteachers.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shraddha on 3/18/2017.
 */

public class ClassTimetableBean implements Serializable {

    private String room_no,subjectId,period,day,classId,empId,rc_status,br_id,division,lec_type,subject_Id,lec_takenas,fromtime,totime,year,batch_group_id,hr,subject,emp_name;

    private ArrayList<ClassTimetableBean> mon;
    private ArrayList<ClassTimetableBean> tue;
    private ArrayList<ClassTimetableBean> wed;
    private ArrayList<ClassTimetableBean> thu;
    private ArrayList<ClassTimetableBean> fri;
    private ArrayList<ClassTimetableBean> sat;

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getRc_status() {
        return rc_status;
    }

    public void setRc_status(String rc_status) {
        this.rc_status = rc_status;
    }

    public String getBr_id() {
        return br_id;
    }

    public void setBr_id(String br_id) {
        this.br_id = br_id;
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

    public String getSubject_Id() {
        return subject_Id;
    }

    public void setSubject_Id(String subject_Id) {
        this.subject_Id = subject_Id;
    }

    public String getLec_takenas() {
        return lec_takenas;
    }

    public void setLec_takenas(String lec_takenas) {
        this.lec_takenas = lec_takenas;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBatch_group_id() {
        return batch_group_id;
    }

    public void setBatch_group_id(String batch_group_id) {
        this.batch_group_id = batch_group_id;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public ArrayList<ClassTimetableBean> getMon() {
        return mon;
    }

    public void setMon(ArrayList<ClassTimetableBean> mon) {
        this.mon = mon;
    }

    public ArrayList<ClassTimetableBean> getTue() {
        return tue;
    }

    public void setTue(ArrayList<ClassTimetableBean> tue) {
        this.tue = tue;
    }

    public ArrayList<ClassTimetableBean> getWed() {
        return wed;
    }

    public void setWed(ArrayList<ClassTimetableBean> wed) {
        this.wed = wed;
    }

    public ArrayList<ClassTimetableBean> getThu() {
        return thu;
    }

    public void setThu(ArrayList<ClassTimetableBean> thu) {
        this.thu = thu;
    }

    public ArrayList<ClassTimetableBean> getFri() {
        return fri;
    }

    public void setFri(ArrayList<ClassTimetableBean> fri) {
        this.fri = fri;
    }

    public ArrayList<ClassTimetableBean> getSat() {
        return sat;
    }

    public void setSat(ArrayList<ClassTimetableBean> sat) {
        this.sat = sat;
    }
}
