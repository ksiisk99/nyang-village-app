package com.uni.aychat.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "UserInfo")
public class UserInfo implements Serializable {
    @PrimaryKey
    @NonNull
    public String studentId;
    public String token;
    public String suspendedDate;
    public int autoLogin;
    public String jwt;

    public UserInfo(@NonNull String studentId, String token, String suspendedDate,int autoLogin, String jwt) {
        this.studentId = studentId;
        this.token = token;
        this.suspendedDate = suspendedDate;
        this.autoLogin=autoLogin;
        this.jwt=jwt;
    }
}
