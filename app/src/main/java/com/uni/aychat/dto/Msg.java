package com.uni.aychat.dto;

import com.google.gson.annotations.SerializedName;

public class Msg {
    @SerializedName(value = "roomId")
    private String roomId;
    @SerializedName(value = "nickName")
    private String nickName;
    @SerializedName(value = "content")
    private String content;
    @SerializedName(value = "time")
    private String time;
    public Msg(String roomId, String nickName, String content, String time) {
        super();
        this.roomId = roomId;
        this.nickName = nickName;
        this.content = content;
        this.time = time;
    }
    public String getRoomId() {
        return roomId;
    }
    public String getNickName() {
        return nickName;
    }
    public String getContent() {
        return content;
    }
    public String getTime() {
        return time;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
