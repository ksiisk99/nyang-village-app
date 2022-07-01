package com.uni.aychat.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

//방에 대한 정보
public class RoomInfo implements Serializable {
    String roomName; //과목이름
    int roomId; //과목인덱스 서버에서 채팅방 인덱스에 해당함
    String nickName; //채팅방 안에서 내 랜덤닉네임
    String professorName; //과목에 대한 교수이름
    ArrayList<String> roomInNames; //채팅방 안에 있는 유저 닉네임

    public RoomInfo(String roomName, int roomId, String nickName, ArrayList<String> roomInNames) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.nickName = nickName;
        this.roomInNames = roomInNames;
    }

    public RoomInfo(String roomName, int roomId, String nickName, String professorName, ArrayList<String> roomInNames) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.nickName = nickName;
        this.professorName = professorName;
        this.roomInNames = roomInNames;
    }

    public RoomInfo(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ArrayList<String> getRoomInNames() {
        return roomInNames;
    }

    public void setRoomInNames(ArrayList<String> roomInNames) {
        this.roomInNames = roomInNames;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInfo roomInfo = (RoomInfo) o;
        return roomId == roomInfo.roomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}
