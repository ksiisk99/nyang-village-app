package com.uni.aychat.dto;

import com.google.gson.annotations.SerializedName;

public class ReportMsg {
    @SerializedName(value = "reportName")
    String reportName;
    @SerializedName(value = "reportContent")
    String reportContent;
    @SerializedName(value = "reportWhy")
    String reportWhy;
    @SerializedName(value = "reporter")
    String reporter;
    @SerializedName(value = "studentId")
    String studentId;
    @SerializedName(value = "roomId")
    int roomId;

    public ReportMsg(String reportName, String reportContent, String reportWhy, String reporter, String studentId,int roomId) {
        this.reportName = reportName;
        this.reportContent = reportContent;
        this.reportWhy = reportWhy;
        this.reporter = reporter;
        this.studentId = studentId;
        this.roomId=roomId;
    }

    public String getReportName() {
        return reportName;
    }

    public String getReportContent() {
        return reportContent;
    }

    public String getReportWhy() {
        return reportWhy;
    }

    public String getReporter() {
        return reporter;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getRoomId(){
        return roomId;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public void setReportWhy(String reportWhy) {
        this.reportWhy = reportWhy;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
