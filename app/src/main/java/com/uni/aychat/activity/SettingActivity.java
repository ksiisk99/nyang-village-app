package com.uni.aychat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uni.aychat.R;
import com.uni.aychat.db.RoomDB;
import com.uni.aychat.db.RoomInfoDao;

public class SettingActivity extends AppCompatActivity {
    private Button logoutBtn;
    private RoomDB db;
    private RoomInfoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logoutBtn=findViewById(R.id.logoutBtn);
        db=RoomDB.getInstance(getApplicationContext());
        dao=db.getRoomInfoDao();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.DeleteAllRoomInfos();
                dao.DeleteAllRoomInNames();
                dao.DeleteChatInfos();
                dao.UpdateUserInfoAutoLogin(0);
                //dao.DeleteUserInfo(); //채팅방은 유지하고 자동로그인만 없애기 위해 유저정보만 삭제한다.
                Intent intent=new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}