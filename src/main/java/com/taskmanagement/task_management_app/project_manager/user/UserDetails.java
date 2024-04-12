package com.taskmanagement.task_management_app.project_manager.user;

public class UserDetails {
    private String user_id;
    private String fname;
    private String lname;
    private String phone_num;
    private String email;
    private String dob;
    private String sex;
    private String address;
    private String user_roll;
    private String password;

    public UserDetails(String user_id, String fname, String lname, String phone_num, String email, String dob, String sex, String address, String user_roll, String password) {
        this.user_id = user_id;
        this.fname = fname;
        this.lname = lname;
        this.phone_num = phone_num;
        this.email = email;
        this.dob = dob;
        this.sex = sex;
        this.address = address;
        this.user_roll = user_roll;
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_roll() {
        return user_roll;
    }

    public void setUser_roll(String user_roll) {
        this.user_roll = user_roll;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
