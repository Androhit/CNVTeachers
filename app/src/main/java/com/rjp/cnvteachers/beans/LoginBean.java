package com.rjp.cnvteachers.beans;

/**
 * Created by rohit on 26/10/16.
 */
public class LoginBean
{
    private String empid,result,name,user_name,user_pass, firstname, middlename, lastname, fathername,sex,br_id,department;
    private String email,address,city,country,zip,phone, birthdate,caste,religion,qualification,experience;
    private boolean status;
    private String designation,designation_id;
    private String acadyear,photo_url;
    private LoginBean emp_details;

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public LoginBean getEmp_details() {
        return emp_details;
    }

    public void setEmp_details(LoginBean emp_details) {
        this.emp_details = emp_details;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEmpid() {
        return empid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {        this.middlename = middlename;   }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) { this.lastname = lastname;  }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBr_id() {
        return br_id;
    }

    public void setBr_id(String br_id) {
        this.br_id = br_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getReligion() {
        return religion;
    }

    public void setRelegion(String religion) {
        this.religion = religion;
    }

    public String getDepartment() {
        return department;
    }

    public void setQualification(String qualification) {  this.qualification = qualification;   }

    public String getQualification() { return qualification;  }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(String designation_id) {
        this.designation_id = designation_id;
    }
}
