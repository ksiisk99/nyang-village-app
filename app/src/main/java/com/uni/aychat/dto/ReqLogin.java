package com.uni.aychat.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReqLogin{
    @SerializedName(value = "version")
    private int version;
    @SerializedName(value = "fcm")
    private String fcm;
    @SerializedName(value = "studentId")
    private String studentId;
    @SerializedName(value = "password")
    private String password;
    @SerializedName(value = "userSubjects")
    private ArrayList<String> userSubjects;

    public ReqLogin(){}

    public ReqLogin(int version, String fcm, String studentId, String password) {
        this.version = version;
        this.fcm = fcm;
        this.studentId = studentId;
        this.password = password;
    }

    public ReqLogin(int version, String fcm, String studentId, String password, ArrayList<String> userSubjects) {
        this.version = version;
        this.fcm = fcm;
        this.studentId = studentId;
        this.password = password;
        this.userSubjects = userSubjects;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setUserSubjects(ArrayList<String> userSubjects) {
        this.userSubjects = userSubjects;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVersion() {
        return version;
    }

    public String getFcm() {
        return fcm;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getUserSubjects() {
        return userSubjects;
    }
}