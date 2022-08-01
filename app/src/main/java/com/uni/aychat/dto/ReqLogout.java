package com.uni.aychat.dto;

import com.google.gson.annotations.SerializedName;

public class ReqLogout {
    @SerializedName(value = "studentId")
    private String studentId;
    @SerializedName(value = "fcm")
    private String fcm;

    public ReqLogout(){}

    public ReqLogout(String studentId, String fcm) {
        this.studentId = studentId;
        this.fcm = fcm;
    }
}
