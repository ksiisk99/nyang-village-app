package com.uni.aychat.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ChatInfo")
public class ChatInfo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int roomId;
    public String nickName;
    public String time;
    public String content;
    public int type;

    @Ignore
    public ChatInfo(int roomId, String nickName, int type) {
        this.roomId = roomId;
        this.nickName = nickName;
        this.type = type;
    }

    public ChatInfo(int roomId, String nickName, String time, String content, int type) {
        this.roomId = roomId;
        this.nickName = nickName;
        this.time = time;
        this.content = content;
        this.type=type;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return nickName;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
