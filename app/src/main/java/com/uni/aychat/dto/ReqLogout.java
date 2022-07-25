package com.uni.aychat.dto;

import com.google.gson.annotations.SerializedName;

public class ReqLogout {
    @SerializedName(value = "studentId")
    private String studentId;

    public ReqLogout(){}
    public ReqLogout(String studentId) {
        this.studentId = studentId;
    }

}
