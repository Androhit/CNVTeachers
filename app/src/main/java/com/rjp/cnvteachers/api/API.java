package com.rjp.cnvteachers.api;


import com.google.gson.JsonElement;
import com.rjp.cnvteachers.beans.ApiResults;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Rohit
 */
public interface API
{

    @FormUrlEncoded
    @POST("/get_app_institutes.php")
    void validate_institute(@Field("code") String code, Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/get_class.php?method_name=all_class")
    void getClass_list(@Field("code") String code,  Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/get_divison.php?method_name=all_division")
    void getDivison_list(
            @Field("code") String code,
            @Field("class_id") String class_id,
            Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/get_AcadYr.php?method_name=all_AcadYr")
    void getAcad_year(@Field("code") String code, @Field("br_id") String br_id,Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/exam_name.php?method_name=ExamName")
    void getexam_name(@Field("code") String code,
                      @Field("class_id") String class_id,
                      @Field("division") String division,
                      @Field("br_id") String br_id,
                      @Field("acad_year") String acad_year,
                      Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/get_AdmYr.php?method_name=all_AdmYr")
    void getAdm_year(@Field("code") String code, @Field("br_id") String br_id,Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/get_stud_list.php?method_name=all_students")
    void getStudent_list(@Field("code") String code,@Field("firstname") String firstname,@Field("class_id") String class_id,@Field("division_name") String division_name,@Field("admno") String admno,Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/authenticate.php?method_name=authenticate")
    void getLogin(@Field("code") String code, @Field("br_id") String br_id, @Field("nick") String username, @Field("password") String password, Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/get_employee_profile.php?method_name=show_profile")//show_profile
    void get_Employee_Profile(
                        @Field("code") String code,
                        @Field("empid") String empid,
                        Callback<ApiResults> taskResults);


    @FormUrlEncoded
    @POST("/class_timetable.php?method_name=show_class_timetable")
    void getClassTimeTable(
            @Field("code") String code,
            @Field("class_id") String class_id,
            @Field("branch_id") String branch_id,
            @Field("division") String division,
            @Field("acad_year") String acad_year,
            Callback<ApiResults> taskResults);


    @FormUrlEncoded
    @POST("/performance.php?method_name=All_Performances")
    void getPerformance(
            @Field("code") String code,
            @Field("class_id") String class_id,
            @Field("division") String division,
            @Field("branch_id") String branch_id,
            @Field("acad_year") String acad_year,
            @Field("adm_year") String adm_year,
            @Field("exam_id") String exam_id,
            Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/detail_exam_result.php?method_name=All_Result")
    void getExamResult(
            @Field("code") String code,
            @Field("exam_id") String exam_id,
            @Field("admno") String admno,
            @Field("acad_year") String acad_year,
            @Field("branch_id") String branch_id,
            Callback<ApiResults> taskResults);


    @FormUrlEncoded
    @POST("/my_timetable.php?method_name=show_my_timetable")
    void getMyTimeTable(
            @Field("code") String code,
            @Field("empid") String empid,
            @Field("branch_id") String branch_id,
            @Field("acad_year") String acad_year,
            Callback<ApiResults> taskResults);



    @FormUrlEncoded
    @POST("/update_employee_profile.php?method_name=update_profile")//update_profile
    void update_Employee_Profile(
            @Field("code") String code,
            @Field("empid") String empid,
            @Field("experience") String experience,
            @Field("qualification") String qualification,
            @Field("emg_phone") String emg_phone,
            @Field("bgroup") String bgroup,
            @Field("birthdate") String birthdate,
            @Field("reg_date") String reg_date,
            @Field("address") String address,
            @Field("sex") String sex,
            @Field("phone") String phone,
            @Field("religion") String religion,
            @Field("permanent_address") String permanent_address,
            Callback<JsonElement> taskResults);


    @FormUrlEncoded
    @POST("/get_student_Attendance.php?method_name=Student_Attendance")
    void getAttendance(
            @Field("code") String code,
            @Field("name") String name,
            @Field("admno") String admno,
            @Field("fromdate") String fromdate,
            @Field("todate") String todate,
            @Field("branch_id") String branch_id,
            @Field("acad_year") String acad_year,
            Callback<ApiResults> taskResults);


    @FormUrlEncoded
    @POST("/get_Student.php?method_name=Student")
    void getStudent(
            @Field("code") String code,
            Callback<ApiResults> taskResults);


    @FormUrlEncoded
    @POST("/get_Admno.php?method_name=Admno")
    void getAdmno(
            @Field("code") String code,
            Callback<ApiResults> taskResults);


    @FormUrlEncoded
    @POST("/exam_info.php")
    void getExamResults(@Field("method_name") String method_name,@Field("branch_id") String branch_id, Callback<ApiResults> leaveList);

    @FormUrlEncoded
    @POST("/get_exam_info.php?method_name=exam_schedule")
    void getExamInfo(@Field("code") String code,
                     @Field("class_id") String class_id,
                     @Field("branch_id") String branch_id,
                     @Field("acad_year") String acad_year,
                     Callback<ApiResults> leaveList);

    @FormUrlEncoded
    @POST("/get_exam_detail_schedule.php?method_name=examdetail_schedule")
    void getExamDetailInfo(@Field("code") String code,
                           @Field("exam_id") String exam_id,
                           @Field("branch_id") String branch_id,
                           @Field("acad_year") String acad_year,
                           Callback<ApiResults> leaveList);

    @FormUrlEncoded
    @POST("/get_circular.php?method_name=circular_info")
    void get_Circular(@Field("code") String code,
                     @Field("branch_id") String branch_id,
                     @Field("acad_year") String acad_year,
                     @Field("class_id") String class_id,
                     Callback<ApiResults> leaveList);


    @FormUrlEncoded
    @POST("/get_circular_details.php?method_name=circular_info_indet")
    void getNoticeDetails(@Field("code") String code,
                      @Field("br_id") String br_id,
                      @Field("id") String id,
                      Callback<ApiResults> leaveList);


    @FormUrlEncoded
    @POST("/get_class_Attendance.php?method_name=Class_Attendance")
    void getClassAttendance(
            @Field("code") String code,
            @Field("class_id") String class_id,
            @Field("division_name") String division_name,
            @Field("branch_id") String branch_id,
            @Field("acad_year") String acad_year,
            @Field("fromdate") String fromdate,
            @Field("todate") String todate,
            Callback<ApiResults> taskResults);

    @FormUrlEncoded
    @POST("/student_achievment.php?method_name=special_achiv")
    void getStudentAchievmentData(
            @Field("code") String code,
            @Field("Stud_name") String Stud_name,
            @Field("admno") String admno,
            @Field("branch_id") String branch_id,
            @Field("acad_year") String acad_year,
            Callback<ApiResults> leaveList);

    @FormUrlEncoded
    @POST("/get_hos.php?method_name=hos_info")
    void getHandsonList(
            @Field("code") String code,
            @Field("name") String name,
            @Field("admno") String admno,
            @Field("class_id") String class_id,
            @Field("division_name") String division_name,
            @Field("empid") String empid,
            @Field("branch_id") String branch_id,
            @Field("acad_year") String acad_year,
            Callback<ApiResults> leaveList);


}