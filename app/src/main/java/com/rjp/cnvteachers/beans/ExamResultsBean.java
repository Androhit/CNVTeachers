package com.rjp.cnvteachers.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shraddha on 3/23/2017.
 */

public class ExamResultsBean implements Serializable {

    private String sub_id,sub_name,marks,max_marks,percentage,grade,stud_name;
    private String total_marks,obtained_marks,total_percentage,total_grade;

    ArrayList<ExamResultsBean> data_array = new ArrayList<>();


    public ArrayList<ExamResultsBean> getData_array() {
        return data_array;
    }


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

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getMax_marks() {
        return max_marks;
    }

    public void setMax_marks(String max_marks) {
        this.max_marks = max_marks;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getObtained_marks() {
        return obtained_marks;
    }

    public void setObtained_marks(String obtained_marks) {
        this.obtained_marks = obtained_marks;
    }

    public String getTotal_percentage() {
        return total_percentage;
    }

    public void setTotal_percentage(String total_percentage) {
        this.total_percentage = total_percentage;
    }

    public String getTotal_grade() {
        return total_grade;
    }

    public void setTotal_grade(String total_grade) {
        this.total_grade = total_grade;
    }
}

