package com.uni.aychat.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResLogin {
    @SerializedName("signal")
    int signal;
    @SerializedName("suspendedDate")
    String suspendedDate;
    @SerializedName("roomInfos")
    ArrayList<RoomInfo> roomInfos;
    @SerializedName("jwt")
    String jwt;

    public ResLogin(){}

    public ResLogin(int signal, String suspendedDate, ArrayList<RoomInfo> roomInfos, String jwt) {
        this.signal = signal;
        this.suspendedDate = suspendedDate;
        this.roomInfos = roomInfos;
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public String getSuspendedDate() {
        return suspendedDate;
    }

    public void setSuspendedDate(String suspendedDate) {
        this.suspendedDate = suspendedDate;
    }

    public ArrayList<RoomInfo> getRoomInfos() {
        return roomInfos;
    }

    public void setRoomInfos(ArrayList<RoomInfo> roomInfos) {
        this.roomInfos = roomInfos;
    }
}
