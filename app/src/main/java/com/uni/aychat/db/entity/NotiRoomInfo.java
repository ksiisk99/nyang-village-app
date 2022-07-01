package com.uni.aychat.db.entity;

public class NotiRoomInfo {
    String roomName; //과목이름
    int noti;

    public NotiRoomInfo(String roomName, int noti) {
        this.roomName = roomName;
        this.noti = noti;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getNoti() {
        return noti;
    }
}
