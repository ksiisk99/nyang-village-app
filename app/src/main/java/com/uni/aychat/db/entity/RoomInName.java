package com.uni.aychat.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "RoomInName")
public class RoomInName implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int roomId;
    public String name;

    public RoomInName(int roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
}