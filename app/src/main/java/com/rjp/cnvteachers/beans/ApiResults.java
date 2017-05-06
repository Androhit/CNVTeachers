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
    public ArrayList<StudentBean> stud;
    public ArrayList<StudentBean> studList;
    public ArrayList<SubjectBean> Subjects;
    public ArrayList<TopicBean> Topics;
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
    public ArrayList<ExamResultsBean> exm_all_sub;
    public String total_grade;
    public String empname,empid,att_id,chklist;
    public ArrayList<ExamDetailsBeans> examdetails_list;
    public ArrayList<ExamBean> exam_list;
    public ArrayList<ExamBean> all_exam_name;
    public ArrayList<ExamBean> exam;
    public ExamResultsBean stud_result;
    public ArrayList<CircularBean> circular_info;
    public ArrayList<RemarkBean> Remarks;
    public ArrayList<CircularBean> circular_info_det;
    public ArrayList<AchievementsBean> special_achiv;
    public ArrayList<HandsOnScienceBeans> hos_info;
    public ArrayList<AdmissionYearBean> adm_yr_list;
    public ArrayList<AcademicYearBean> acad_yr_list;
    public ArrayList<WorksheetBean> worksheet_list;
    public ArrayList<ParentBean> parent_list;
    public String total_percentage,total_marks,obtained_marks;
    public  ArrayList<FcmNotificationBean> recent_chats;
    public  ArrayList<FcmNotificationBean> message_list;
    public  ArrayList<MessageTitlesBean> titles_list;
    private ArrayList<NotificationBeans> fcm_notifications;
    private StudentBean Students_Count;
    private String Total_stud;

    public String getTotal_stud() {
        return Total_stud;
    }

    public void setTotal_stud(String total_stud) {
        Total_stud = total_stud;
    }

    public StudentBean getStudents_Count() {
        return Students_Count;
    }

    public void setStudents_Count(StudentBean students_Count) {
        Students_Count = students_Count;
    }

    public ArrayList<MessageTitlesBean> getTitles_list() {
        return titles_list;
    }

    public void setTitles_list(ArrayList<MessageTitlesBean> titles_list) {
        this.titles_list = titles_list;
    }

    public ArrayList<FcmNotificationBean> getMessage_list() {
        return message_list;
    }

    public void setMessage_list(ArrayList<FcmNotificationBean> message_list) {
        this.message_list = message_list;
    }

    public ArrayList<FcmNotificationBean> getRecent_chats() {
        return recent_chats;
    }

    public void setRecent_chats(ArrayList<FcmNotificationBean> recent_chats) {
        this.recent_chats = recent_chats;
    }

    public ArrayList<RemarkBean> getRemarks() {
        return Remarks;
    }

    public void setRemarks(ArrayList<RemarkBean> remarks) {
        Remarks = remarks;
    }

    public void setMsg(EmployeeBean msg) {
        this.msg = msg;
    }

    public String getChklist() {
        return chklist;
    }

    public void setChklist(String chklist) {
        this.chklist = chklist;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getAtt_id() {
        return att_id;
    }

    public void setAtt_id(String att_id) {
        this.att_id = att_id;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public ExamResultsBean getStud_result() {
        return stud_result;
    }

    public void setStud_result(ExamResultsBean stud_result) {
        this.stud_result = stud_result;
    }

    public String getObtained_marks() {
        return obtained_marks;
    }

    public void setObtained_marks(String obtained_marks) {
        this.obtained_marks = obtained_marks;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getTotal_percentage() {
        return total_percentage;
    }

    public void setTotal_percentage(String total_percentage) {
        this.total_percentage = total_percentage;
    }

    public ArrayList<ExamBean> getAll_exam_name() {
        return all_exam_name;
    }

    public void setAll_exam_name(ArrayList<ExamBean> all_exam_name) {
        this.all_exam_name = all_exam_name;
    }

    public ArrayList<ExamBean> getExam() {
        return exam;
    }

    public void setExam(ArrayList<ExamBean> exam) {
        this.exam = exam;
    }

    public ArrayList<AcademicYearBean> getAcad_yr_list() {
        return acad_yr_list;
    }

    public void setAcad_yr_list(ArrayList<AcademicYearBean> acad_yr_list) {
        this.acad_yr_list = acad_yr_list;
    }

    public ArrayList<AdmissionYearBean> getAdm_yr_list() {
        return adm_yr_list;
    }

    public void setAdm_yr_list(ArrayList<AdmissionYearBean> adm_yr_list) {
        this.adm_yr_list = adm_yr_list;
    }

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


    public ArrayList<ExamResultsBean> getExm_all_sub() {
        return exm_all_sub;
    }

    public void setExm_all_sub(ArrayList<ExamResultsBean> exm_all_sub) {
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

    public ArrayList<StudentBean> getStud() {
        return stud;
    }

    public void setStud(ArrayList<StudentBean> stud) {
        this.stud = stud;
    }

    public ArrayList<WorksheetBean> getWorksheet_list() {
        return worksheet_list;
    }

    public void setWorksheet_list(ArrayList<WorksheetBean> worksheet_list) {
        this.worksheet_list = worksheet_list;
    }

    public ArrayList<StudentBean> getStudList() {
        return studList;
    }

    public void setStudList(ArrayList<StudentBean> studList) {
        this.studList = studList;
    }

    public ArrayList<SubjectBean> getSubjects() {
        return Subjects;
    }

    public void setSubjects(ArrayList<SubjectBean> subjects) {
        Subjects = subjects;
    }

    public ArrayList<TopicBean> getTopics() {
        return Topics;
    }

    public void setTopics(ArrayList<TopicBean> topics) {
        Topics = topics;
    }

    public ArrayList<ParentBean> getParent_list() {
        return parent_list;
    }

    public void setParent_list(ArrayList<ParentBean> parent_list) {
        this.parent_list = parent_list;
    }

    public ArrayList<NotificationBeans> getFcm_notifications() {
        return fcm_notifications;
    }

    public void setFcm_notifications(ArrayList<NotificationBeans> fcm_notifications) {
        this.fcm_notifications = fcm_notifications;
    }
}
