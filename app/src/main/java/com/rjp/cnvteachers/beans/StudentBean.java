package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 3/8/2017.
 */

public class StudentBean implements Serializable {
    private String name,firstname,lastname,middlename,fathername,mothername,sex,rollno,religion,stu_class;
    private String class_name,address1,address2,emergencontact,caste,division,fee_category;
    private String year_admission,birthdate,phone,state,father_mobile,mother_mobile,nationality;
    private String username,password,admno,photo_url,grno,birth_place,parent_email,foccupation,moccupation,foffice_add,moffice_add,bgroup;
    private boolean selected;

    public StudentBean()
    {
        selected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getStu_class() {
        return stu_class;
    }

    public void setStu_class(String stu_class) {
        this.stu_class = stu_class;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getEmergencontact() {
        return emergencontact;
    }

    public void setEmergencontact(String emergencontact) {
        this.emergencontact = emergencontact;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getFee_category() {
        return fee_category;
    }

    public void setFee_category(String fee_category) {
        this.fee_category = fee_category;
    }

    public String getYear_admission() {
        return year_admission;
    }

    public void setYear_admission(String year_admission) {
        this.year_admission = year_admission;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFather_mobile() {
        return father_mobile;
    }

    public void setFather_mobile(String father_mobile) {
        this.father_mobile = father_mobile;
    }

    public String getMother_mobile() {
        return mother_mobile;
    }

    public void setMother_mobile(String mother_mobile) {
        this.mother_mobile = mother_mobile;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdmno() {
        return admno;
    }

    public void setAdmno(String admno) {
        this.admno = admno;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getGrno() {
        return grno;
    }

    public void setGrno(String grno) {
        this.grno = grno;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    public String getParent_email() {
        return parent_email;
    }

    public void setParent_email(String parent_email) {
        this.parent_email = parent_email;
    }

    public String getFoccupation() {
        return foccupation;
    }

    public void setFoccupation(String foccupation) {
        this.foccupation = foccupation;
    }

    public String getMoccupation() {
        return moccupation;
    }

    public void setMoccupation(String moccupation) {
        this.moccupation = moccupation;
    }

    public String getFoffice_add() {
        return foffice_add;
    }

    public void setFoffice_add(String foffice_add) {
        this.foffice_add = foffice_add;
    }

    public String getMoffice_add() {
        return moffice_add;
    }

    public void setMoffice_add(String moffice_add) {
        this.moffice_add = moffice_add;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
