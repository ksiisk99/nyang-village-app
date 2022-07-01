package com.uni.aychat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.uni.aychat.R;
import com.uni.aychat.adapter.ListAdapter;
import com.uni.aychat.db.RoomDB;
import com.uni.aychat.db.RoomInfoDao;
import com.uni.aychat.dto.RoomInfo;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ArrayList<RoomInfo> roomInfos;
    private RoomDB db;
    private RoomInfoDao dao;
    private Button settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db=RoomDB.getInstance(getApplicationContext());
        dao=db.getRoomInfoDao();
        roomInfos=(ArrayList<RoomInfo>)getIntent().getSerializableExtra("roomInfos"); //방정보
        if(getIntent().getExtras().getInt("notiClick")==1){
            Intent intent=new Intent(ListActivity.this, ChatActivity.class);
            int roomId=getIntent().getExtras().getInt("roomId");
            int idx=roomInfos.indexOf(new RoomInfo(roomId));
            intent.putExtra("nickName",roomInfos.get(idx).getNickName());
            intent.putExtra("roomName",roomInfos.get(idx).getRoomName());
            intent.putExtra("roomId",roomId);
            intent.putExtra("professorName",roomInfos.get(idx).getProfessorName());
            startActivity(intent);
        }


        settingBtn=findViewById(R.id.settingBtn);

        textView=findViewById(R.id.textView);
        recyclerView=findViewById(R.id.recyclerView);

        listAdapter=new ListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent=new Intent(ListActivity.this,ChatActivity.class);
                intent.putExtra("nickName",roomInfos.get(position).getNickName());
                intent.putExtra("roomName",roomInfos.get(position).getRoomName());
                intent.putExtra("roomId",roomInfos.get(position).getRoomId());
                intent.putExtra("professorName",roomInfos.get(position).getProfessorName());
                startActivity(intent);
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }


    //나갔다가 들어와도 채팅방에 대한 유저수 변환을 줘야한다.
    @Override
    protected void onStart() {
        super.onStart();
        for(int i=0;i<roomInfos.size();i++){
            roomInfos.get(i).setRoomInNames((ArrayList<String>)dao.SelectRoomInNames2(roomInfos.get(i).getRoomId()));
        }
        listAdapter.setRoomInfos(roomInfos);
    }
}