package com.uni.aychat.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.uni.aychat.etc.OnSingleClickListener;
import com.uni.aychat.etc.ProgressDialog;
import com.uni.aychat.R;
import com.uni.aychat.db.entity.ChatInfo;
import com.uni.aychat.db.RoomDB;
import com.uni.aychat.db.entity.RoomInName;
import com.uni.aychat.db.RoomInfoDao;
import com.uni.aychat.db.entity.UserInfo;
import com.uni.aychat.dto.ResLogin;
import com.uni.aychat.dto.RoomInfo;
import com.uni.aychat.dto.ReqLogin;
import com.uni.aychat.retrofit.RetrofitService;

import java.util.ArrayList;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private String token;
    private EditText studentId, password;
    private Button submit;
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private RoomDB db;
    private RoomInfoDao dao;
    private Button btn;

    private final int VERSION=1;
    //업데이트 신호 1
    private final int updateSignal=1;
    //처음 로그인 신호 3
    private final int firstLoginSignal=3;
    //같은 기기로 로그아웃하고 로그인한 회원 신호 4
    private final int reviseSubjectSignal=4;
    //이중로그인 신호 4
    private final int doubleLoginSignal=4;
    //아이디 비밀번호가 잘못 입력됨 신호 5
    private final int misspellSignal=5;
    //정지회원 신호 6
    private final int suspendedSignal=6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = RoomDB.getInstance(getApplicationContext());
        dao = db.getRoomInfoDao();
        token = dao.SelectUserInfoToken();
//        dao.DeleteAllRoomInfos();
//        dao.DeleteAllRoomInNames();
//        //dao.UpdateUserInfoAutoLogin(0);
//        dao.DeleteUserInfo();
//        dao.DeleteChatInfos();

        if (token == null)
            getToken();
//        if (dao.SelectUserInfoSuspendedDate() != null) { //정지회원 이라면
//            if (Integer.parseInt(dao.SelectUserInfoSuspendedDate()) >= Integer.parseInt(getCurrentTime())) {
//                Toast.makeText(getApplicationContext(), dao.SelectUserInfoSuspendedDate(), Toast.LENGTH_LONG).show();
//                finish();
//            } else {//정지 풀림
//                dao.UpdateUserInfoSuspendedDate(null);
//            }
//        }
        if (dao.SelectUserInfoAutoLogin() == 1) {//자동로그인
            Autologin();
        }

        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);


        progressDialog = new ProgressDialog(MainActivity.this); //로딩창 객체
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);


        retrofit = new Retrofit.Builder() //레트로핏 http통신
                .baseUrl(getString(R.string.http_server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //.addConverterFactory(JacksonConverterFactory.create(jacksonMapper))

        retrofitService = retrofit.create(RetrofitService.class);

        studentId = findViewById(R.id.studentId);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (token != null) {
                    String id = studentId.getText().toString();
                    String pw = password.getText().toString();
                    if (!id.equals("") && !pw.equals("")) {
                        progressDialog.show();
                        Login(id, pw);
                    } else {
                        Toast.makeText(getApplicationContext(), "학번 혹은 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "잠시 후 클릭해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void Autologin(){
        ArrayList<RoomInfo> rstRoomInfos=new ArrayList<>();
        //UserInfo userInfo=dao.SelectUserInfo();
        List<com.uni.aychat.db.entity.RoomInfo> roomInfos=dao.SelectRoomInfos();
        for(int i=0;i<roomInfos.size();i++){
            ArrayList<String> roomInNames= (ArrayList<String>) dao.SelectRoomInNames2(roomInfos.get(i).getRoomId());
//            for(int j=0;j<roomInNames.size();j++){
//                Log.d("name ",roomInNames.get(j));
//            }
            RoomInfo roomInfo=new RoomInfo(roomInfos.get(i).getRoomName(), roomInfos.get(i).getRoomId(), roomInfos.get(i).getNickName(),roomInfos.get(i).getProfessorName(), roomInNames);
            rstRoomInfos.add(roomInfo);
        }

        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        intent.putExtra("roomInfos",rstRoomInfos);
        Intent notiIntent=getIntent();
        if(notiIntent.getExtras()!=null){
            intent.putExtra("notiClick",1);
            intent.putExtra("roomId",notiIntent.getExtras().getInt("roomId"));
        }
        startActivity(intent);
        finish();
    }


    private void Login(String studentId, String password){
        ReqLogin reqLogin=new ReqLogin();
        reqLogin.setFcm(token);
        //reqLogin.setCurrentDate(getCurrentTime());
        reqLogin.setStudentId(studentId);
        reqLogin.setVersion(VERSION);
        reqLogin.setPassword(password);

        Call<ResLogin> callLogin=retrofitService.reqLogin(reqLogin);
        //로그인 요청
        callLogin.enqueue(new Callback<ResLogin>() {
            @Override
            public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                if(response.isSuccessful()){
                    dao.DeleteAllRoomInfos();
                    dao.DeleteAllRoomInNames();
                    dao.DeleteUserInfo();
                    dao.DeleteChatInfos();
                    int signal=response.body().getSignal();
                    if(signal==updateSignal){ //업데이트 신호
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"업데이트 후 사용 가능합니다.",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else if(signal==2){ //로그아웃 신호
                        Logout();
                    }else if(signal==firstLoginSignal){ //처음 로그인 성공
                        ArrayList<RoomInfo> tmpRoomInfos=response.body().getRoomInfos();
                        List<com.uni.aychat.db.entity.RoomInfo> rstRoomInfos=new ArrayList<>(); //list로 db에 저장하기 위해 변수 선언

                        for(int i=0;i<tmpRoomInfos.size();i++){
                            int roomId=tmpRoomInfos.get(i).getRoomId();
                            ArrayList<String> tmpRoomInNames=tmpRoomInfos.get(i).getRoomInNames();

                            List<RoomInName> rst=new ArrayList<>();
                            for(int j=0;j<tmpRoomInNames.size();j++){
                                RoomInName roomInName=new RoomInName(roomId,tmpRoomInNames.get(j));
                                rst.add(roomInName);
                            }
                            dao.InsertRoomInNames(rst); //db에 방 유저들 닉네임 저장
                            com.uni.aychat.db.entity.RoomInfo roomInfo=new com.uni.aychat.db.entity.RoomInfo(tmpRoomInfos.get(i).getRoomId(),tmpRoomInfos.get(i).getRoomName(),tmpRoomInfos.get(i).getNickName(),tmpRoomInfos.get(i).getProfessorName(),0,1);
                            rstRoomInfos.add(roomInfo);
                            ChatInfo chatInfo=new ChatInfo(roomId
                                    ,"",null,"안양대 오픈 채팅방에 오신 걸 환영합니다.\n도배, 욕설 등 불편한 언어 사용 시 상대방의 채팅 메시지를 길게 누르시면 신고하실 수 있습니다.",1);//처음 방 입장 시공지사항
                            dao.InsertChatInfo(chatInfo);
                        }
                        dao.InsertRoomInfos(rstRoomInfos);
                        //dao.UpdateUserInfoAutoLogin(1);
                        //dao.UpdateUserInfoStudentId(studentId);
                        dao.InsertUserInfo(new UserInfo(studentId,token,null,1,response.body().getJwt())); //db에 유저정보 저장

                        Intent intent=new Intent(MainActivity.this,ListActivity.class);
                        intent.putExtra("roomInfos",response.body().getRoomInfos()); //db에 방정보 저장
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        });
                        startActivity(intent);
                        finish();
                    }else if(signal==reviseSubjectSignal){ //로그아웃하고 로그인한 회원 과목이 바뀔 수도 있다.
                        ArrayList<RoomInfo> tmpRoomInfos=response.body().getRoomInfos();
                        List<com.uni.aychat.db.entity.RoomInfo> rstRoomInfos=new ArrayList<>(); //list로 db에 저장하기 위해 변수 선언
                        List<Integer> tmpRoomIds=new ArrayList<>();
                        for(int i=0;i<tmpRoomInfos.size();i++){
                           int roomId=tmpRoomInfos.get(i).getRoomId();
                            tmpRoomIds.add(roomId);
                            ArrayList<String> tmpRoomInNames=tmpRoomInfos.get(i).getRoomInNames();

                            List<RoomInName> rstRoomInNames=new ArrayList<>();
                            for(int j=0;j<tmpRoomInNames.size();j++){
                                RoomInName roomInName=new RoomInName(roomId,tmpRoomInNames.get(j));
                                rstRoomInNames.add(roomInName);
                            }
                            ChatInfo chatInfo=new ChatInfo(roomId
                                    ,"",null,"안양대 오픈 채팅방에 오신 걸 환영합니다.\n도배, 욕설 등 불편한 언어 사용 시 상대방의 채팅 메시지를 길게 누르시면 신고하실 수 있습니다.",1);//처음 방 입장 시공지사항
                            dao.InsertChatInfo(chatInfo);
                            dao.InsertRoomInNames(rstRoomInNames);
                            com.uni.aychat.db.entity.RoomInfo roomInfo=new com.uni.aychat.db.entity.RoomInfo(tmpRoomInfos.get(i).getRoomId(),tmpRoomInfos.get(i).getRoomName(),tmpRoomInfos.get(i).getNickName(),tmpRoomInfos.get(i).getProfessorName(),0,1);
                            rstRoomInfos.add(roomInfo);

                        }
                        dao.InsertRoomInfos(rstRoomInfos);
                        dao.DeleteRoomInfos(tmpRoomIds);
                        dao.InsertUserInfo(new UserInfo(studentId,token,null,1,response.body().getJwt())); //db에 유저정보 저장
                        //dao.UpdateUserInfoAutoLogin(1);
                        //dao.UpdateUserInfoStudentId(studentId);
                        Intent intent=new Intent(MainActivity.this,ListActivity.class);
                        intent.putExtra("roomInfos",tmpRoomInfos); //db에 방정보 저장
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        });
                        startActivity(intent);
                        finish();
                    } else if(signal==misspellSignal){ //아이디 비밀번호가 잘못 입력됨
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"아이디 혹은 비밀번호를 잘못 입력했습니다.",Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
                    } else if(signal==suspendedSignal){ //정지 회원
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"정지된 회원입니다.",Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResLogin> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"네트워크 연결오류",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void Logout(){

    }

    //현재날짜
//    private String getCurrentTime() {
//        long now = System.currentTimeMillis();
//        TimeZone tz=TimeZone.getTimeZone("Asia/Seoul");
//        Date date = new Date(now);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd", Locale.KOREA);
//        dateFormat.setTimeZone(tz);
//        String getTim = dateFormat.format(date);
//        return String.valueOf(getTim.charAt(0)) + String.valueOf(getTim.charAt(1)) + String.valueOf(getTim.charAt(3)) + String.valueOf(getTim.charAt(4)) + String.valueOf(getTim.charAt(6)) + String.valueOf(getTim.charAt(7));
//    }

    //파베 토큰
    private void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(task.isSuccessful()){
                            token =task.getResult();
                            dao.UpdateUserInfoToken(token);
                            //Log.d("CurrentToken",token);
                        }
                    }
                });
    }
}