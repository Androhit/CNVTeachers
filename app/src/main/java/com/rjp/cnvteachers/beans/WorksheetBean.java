package com.rjp.cnvteachers.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shraddha on 4/15/2017.
 */

public class WorksheetBean implements Serializable {
    private String Aim,Desc,Title,div_id,FromDate,ToDate,empid,student,br_id,acadyear,class_id,classid,division,id,empname,chklist,attachment;
    private ArrayList<String> count;
    private ArrayList<String> student_array;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public ArrayList<String> getStudent_array() {
        return student_array;
    }

    public void setStudent_array(ArrayList<String> student_array) {
        this.student_array = student_array;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getAim() {
        return Aim;
    }

    public void setAim(String aim) {
        Aim = aim;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public String getBr_id() {
        return br_id;
    }

    public void setBr_id(String br_id) {
        this.br_id = br_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getChklist() {
        return chklist;
    }

    public void setChklist(String chklist) {
        this.chklist = chklist;
    }

    public ArrayList<String> getCount() {
        return count;
    }

    public void setCount(ArrayList<String> count) {
        this.count = count;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getDiv_id() {
        return div_id;
    }

    public void setDiv_id(String div_id) {
        this.div_id = div_id;
    }
}
