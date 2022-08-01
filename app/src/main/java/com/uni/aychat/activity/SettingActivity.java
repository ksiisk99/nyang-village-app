package com.uni.aychat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uni.aychat.R;
import com.uni.aychat.db.RoomDB;
import com.uni.aychat.db.RoomInfoDao;
import com.uni.aychat.dto.ReqLogout;
import com.uni.aychat.dto.ResLogout;
import com.uni.aychat.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActivity extends AppCompatActivity {
    private Button logoutBtn;
    private RoomDB db;
    private RoomInfoDao dao;
    private Retrofit retrofit;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logoutBtn=findViewById(R.id.logoutBtn);
        db=RoomDB.getInstance(getApplicationContext());
        dao=db.getRoomInfoDao();

        retrofit = new Retrofit.Builder() //레트로핏 http통신
                .baseUrl(getString(R.string.http_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<ResLogout> callLogout=retrofitService.reqLogout(new ReqLogout(dao.SelectUserInfoStudentId(),dao.SelectUserInfoToken()));
                callLogout.enqueue(new Callback<ResLogout>() {
                    @Override
                    public void onResponse(Call<ResLogout> call, Response<ResLogout> response) {
                        if(response.isSuccessful()){
                            if(response.body().getSignal()==1){
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
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"로그아웃 실패",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResLogout> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"네트워크 연결 오류",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });



            }
        });
    }
}