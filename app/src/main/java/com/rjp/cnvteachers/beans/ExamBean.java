package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 3/23/2017.
 */

public class ExamBean implements Serializable{
    private String exam_id,exam_name,main_exam_name,start_date,class_name,end_date,percentage,grade,exam_types,stud_name,admno,gr_no,acad_year;

    @Override
    public String toString() {
        return exam_name;
    }

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }



    public String getGr_no() {
        return gr_no;
    }

    public void setGr_no(String gr_no) {
        this.gr_no = gr_no;
    }

    public String getExam_types() {
        return exam_types;
    }

    public void setExam_types(String exam_types) {
        this.exam_types = exam_types;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getMainexam_name() {
        return main_exam_name;
    }

    public void setMainexam_name(String main_exam_name) {
        this.main_exam_name = main_exam_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getAdmno() {
        return admno;
    }

    public void setAdmno(String admno) {
        this.admno = admno;
    }

    public String getAcad_year() {
        return acad_year;
    }

    public void setAcad_year(String acad_year) {
        this.acad_year = acad_year;
    }
}
