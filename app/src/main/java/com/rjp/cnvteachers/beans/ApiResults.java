package com.rjp.cnvteachers.beans;

import java.util.ArrayList;
/**
 * Created by rohit on 27/2/17.
 */
public class ApiResults
{
    private String result, message;
    private LoginBean login;
    private InstitutesBean obj_institutes;
    public ArrayList<LoginBean> login_det;
    public ArrayList<StudentBean> student_list;
    public ArrayList<StudentBean> student;
    public ArrayList<AdmissionBean> admno_no;
    public ArrayList<ClassBean> class_list;
    public ArrayList<DivisonBean> divison_list;
    public ArrayList<EmployeeBean> employee_list;
    public EmployeeBean employee_data;
    public EmployeeBean msg;
    public ArrayList<ClassTimetableBean> class_timetable;
    public MyTimeTableBean my_timetable;
    public AttendanceBean stud_att;
    public ArrayList<AttendanceBean> class_att;
    public ArrayList<ExamResults> exm_all_sub;
    public String total_grade;
    public ArrayList<ExamDetailsBeans> examdetails_list;
    public ArrayList<ExamBean> exam_list;
    public ArrayList<CircularBean> circular_info;
    public ArrayList<CircularBean> circular_info_det;
    public ArrayList<AchievementsBean> special_achiv;
    public ArrayList<HandsOnScienceBeans> hos_info;

    public AttendanceBean getStud_att() {
        return stud_att;
    }

    public void setStud_att(AttendanceBean stud_att) {
        this.stud_att = stud_att;
    }

    public ArrayList<HandsOnScienceBeans> getHos_info() {
        return hos_info;
    }

    public void setHos_info(ArrayList<HandsOnScienceBeans> hos_info) {
        this.hos_info = hos_info;
    }

    public ArrayList<AchievementsBean> getSpecial_achiv() {
        return special_achiv;
    }

    public void setSpecial_achiv(ArrayList<AchievementsBean> special_achiv) {
        this.special_achiv = special_achiv;
    }

    public ArrayList<CircularBean> getCircular_info_det() {
        return circular_info_det;
    }

    public void setCircular_info_det(ArrayList<CircularBean> circular_info_det) {
        this.circular_info_det = circular_info_det;
    }


    public ArrayList<CircularBean> getCircular_info() {
        return circular_info;
    }

    public void setCircular_info(ArrayList<CircularBean> circular_info) {
        this.circular_info = circular_info;
    }

    public ArrayList<AdmissionBean> getAdmno_no() {
        return admno_no;
    }

    public void setAdmno_no(ArrayList<AdmissionBean> admno_no) {
        this.admno_no = admno_no;
    }

    public ArrayList<ExamBean> getExam_list() {
        return exam_list;
    }

    public void setExam_list(ArrayList<ExamBean> exam_list) {
        this.exam_list = exam_list;
    }

    public ArrayList<ExamDetailsBeans> getExamdetails_list() {
        return examdetails_list;
    }

    public void setExamdetails_list(ArrayList<ExamDetailsBeans> examdetails_list) {
        this.examdetails_list = examdetails_list;
    }


    public String getTotal_grade() {
        return total_grade;
    }

    public void setTotal_grade(String total_grade) {
        this.total_grade = total_grade;
    }


    public ArrayList<ExamResults> getExm_all_sub() {
        return exm_all_sub;
    }

    public void setExm_all_sub(ArrayList<ExamResults> exm_all_sub) {
        this.exm_all_sub = exm_all_sub;
    }

    public ArrayList<StudentBean> getStudent() {
        return student;
    }

    public void setStudent(ArrayList<StudentBean> student) {
        this.student = student;
    }


    public ArrayList<AttendanceBean> getClass_att() {
        return class_att;
    }

    public void setClass_att(ArrayList<AttendanceBean> class_att) {
        this.class_att=class_att;
    }

    public EmployeeBean getEmployee_msg() {
        return msg;
    }

    public void setEmployee_msg(EmployeeBean employee_msg) {
        this.msg = msg;
    }

    public ArrayList<EmployeeBean> getEmployee_list() {
        return employee_list;
    }

    public void setEmployee_list(ArrayList<EmployeeBean> employee_list) {
        this.employee_list = employee_list;
    }

    public EmployeeBean getEmployee_data() {
        return employee_data;
    }

    public void setEmployee_data(EmployeeBean employee_data) {
        this.employee_data = employee_data;
    }

    public InstitutesBean getObj_institutes() {
        return obj_institutes;
    }

    public void setObj_institutes(InstitutesBean obj_institutes) {
        this.obj_institutes = obj_institutes;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public String getResult() {  return result; }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<LoginBean> getLogin_det() {
        return login_det;
    }

    public void setLogin_det(ArrayList<LoginBean> login_det) {
        this.login_det = login_det;
    }

    public ArrayList<StudentBean> getStudent_list() {  return student_list;    }

    public void setStudent_list(ArrayList<StudentBean> student_list) {   this.student_list = student_list;    }

    public ArrayList<ClassBean> getClass_list() {  return class_list;    }

    public void setClass_list(ArrayList<ClassBean> class_list) {   this.class_list = class_list;    }

    public ArrayList<DivisonBean> getDivison_list() {  return divison_list;    }

    public void setDivison_list(ArrayList<DivisonBean> divison_list) {   this.divison_list = divison_list;    }

    public ArrayList<EmployeeBean> get_Employee_Profile() {  return employee_list;    }

    public void set_Employee_Profile(ArrayList<EmployeeBean> employee_list) {   this.employee_list = employee_list;    }

    public ArrayList<ClassTimetableBean> getClass_timetable() {
        return class_timetable;
    }

    public void setClass_timetable(ArrayList<ClassTimetableBean> class_timetable) {
        this.class_timetable = class_timetable;
    }


    public MyTimeTableBean getMy_timetable() {
        return my_timetable;
    }

    public void setMy_timetable(MyTimeTableBean my_timetable) {
        this.my_timetable = my_timetable;
    }

}
