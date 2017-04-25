package com.rjp.cnvteachers.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shraddha on 3/21/2017.
 */
public class AttendanceBean implements Serializable
{
    private String present_day,absent_days,working_days,percent,from_date,to_date,month;
    private String name,admno,att_id;
    private String empname,chklist,empid,att_date;
    private String tot_present_day,tot_absent_days,tot_working_days,tot_percent,month_name;

    public String getChklist() {
        return chklist;
    }

    public void setChklist(String chklist) {
        this.chklist = chklist;
    }

    ArrayList<AttendanceBean> data_array = new ArrayList<>();

    public ArrayList<AttendanceBean> getData_array() {
        return data_array;
    }

    public void setData_array(ArrayList<AttendanceBean> data_array) {
        this.data_array = data_array;
    }

    public String getPresent_day() {
        return present_day;
    }

    public void setPresent_day(String present_day) {
        this.present_day = present_day;
    }

    public String getAbsent_days() {
        return absent_days;
    }

    public void setAbsent_days(String absent_days) {
        this.absent_days = absent_days;
    }

    public String getWorking_days() {
        return working_days;
    }

    public void setWorking_days(String working_days) {
        this.working_days = working_days;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmno() {
        return admno;
    }

    public void setAdmno(String admno) {
        this.admno = admno;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTot_present_day() {
        return tot_present_day;
    }

    public void setTot_present_day(String tot_present_day) {
        this.tot_present_day = tot_present_day;
    }

    public String getTot_absent_days() {
        return tot_absent_days;
    }

    public void setTot_absent_days(String tot_absent_days) {
        this.tot_absent_days = tot_absent_days;
    }

    public String getTot_working_days() {
        return tot_working_days;
    }

    public void setTot_working_days(String tot_working_days) {
        this.tot_working_days = tot_working_days;
    }

    public String getTot_percent() {
        return tot_percent;
    }

    public void setTot_percent(String tot_percent) {
        this.tot_percent = tot_percent;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getAtt_date() {
        return att_date;
    }

    public void setAtt_date(String att_date) {
        this.att_date = att_date;
    }

    public String getAtt_id() {
        return att_id;
    }

    public void setAtt_id(String att_id) {
        this.att_id = att_id;
    }
}
