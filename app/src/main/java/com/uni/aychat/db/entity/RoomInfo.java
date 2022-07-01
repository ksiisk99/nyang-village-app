package com.uni.aychat.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "RoomInfo", indices = {@Index(value = {"roomName","nickName"}, unique = true)})
public class RoomInfo implements Serializable {
    @PrimaryKey
    public int roomId; //과목인덱스 서버에서 채팅방 인덱스에 해당함
    public String roomName; //과목이름
    public String nickName; //채팅방 안에서 내 랜덤닉네임
    public String professorName; //교수이름
    public int position; //마지막으로 읽은 채팅 인덱스
    public int noti; //알림허용 0:미허용 / 1:허용

    //public RoomInfo(){}

    public RoomInfo(int roomId, String roomName, String nickName, String professorName, int position, int noti) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.nickName = nickName;
        this.professorName = professorName;
        this.position = position;
        this.noti = noti;
    }

//    public RoomInfo(int roomId, String roomName, String nickName, int position, int noti) {
//        this.roomId = roomId;
//        this.roomName = roomName;
//        this.nickName = nickName;
//        this.position = position;
//        this.noti=noti;
//    }
//
//    public RoomInfo(int roomId, String roomName, String nickName) {
//        this.roomId = roomId;
//        this.roomName = roomName;
//        this.nickName = nickName;
//    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
    public String getNickName() {
        return nickName;
    }

    public int getPosition(){return position;}

    public int getNoti(){return noti;}

    public String getProfessorName() {
        return professorName;
    }
}
