package com.uni.aychat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uni.aychat.etc.OnSingleClickListener;
import com.uni.aychat.R;
import com.uni.aychat.adapter.ChatAdapter;
import com.uni.aychat.adapter.ListAdapter;
import com.uni.aychat.adapter.UserListAdapter;
import com.uni.aychat.db.entity.ChatInfo;
import com.uni.aychat.db.RoomDB;
import com.uni.aychat.db.entity.RoomInName;
import com.uni.aychat.db.RoomInfoDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ChatActivity extends AppCompatActivity {
    private final int doubleLoginSignal=1; //이중로그인 신호 1
    private final int suspendedSignal=2; //정지회원 신호 2
    private final int semeterSignal=3; //새학기 신호 3
    private final int updateSignal=4; //업데이트 신호 4
    private final int enterMsgSignal=0; //입장 신호 0
    private final int exitMsgSignal=1; //퇴장 신호 1
    private final int sendMsgSignal=3; //보낸 메시지 신호 3
    private final int receiveMsgSignal=2; //받은 메시지 신호 2
    private final int VERSION=1; //앱 버전
    private List<StompHeader> headerList;
    private StompClient stompClient;
    private String url;
    private Button sendBtn,notiBtn, menuBtn;
    private String nickName,roomName,professorName,studentId,roomId2;
    private ChatAdapter adapter=null;
    private UserListAdapter userListAdapter=null;
    private EditText messageEdit;
    private RoomDB roomDB=null;
    private RoomInfoDao dao=null;
    private int roomId, position, noti;
    private List<ChatInfo> chatInfos;
    private List<String> roomInNames;
    private RecyclerView recyclerView, userListRecyclerView;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private TextView myNickName, titleTextView,personTextView,subjectTextView,professorTextView;
    private Dialog reportDialog;
    private ConnectivityManager connectivityManager;
    private NotificationManager notificationManager;
    private LinearLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendBtn=findViewById(R.id.sendBtn);
        messageEdit=findViewById(R.id.messageEdit);
        recyclerView=findViewById(R.id.recyclerView);
        userListRecyclerView=findViewById(R.id.userlist_recyclerview);
        drawerLayout=findViewById(R.id.drawer_layout);
        myNickName=findViewById(R.id.myNickName);
        notiBtn=findViewById(R.id.notiBtn);
        menuBtn=findViewById(R.id.menuBtn);
        titleTextView=findViewById(R.id.titleTextView);
        personTextView=findViewById(R.id.personTextView);
        subjectTextView=findViewById(R.id.subjectTextView);
        professorTextView=findViewById(R.id.professorTextView);
        reportDialog=new Dialog(ChatActivity.this);
        reportDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reportDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        reportDialog.setContentView(R.layout.report_dialog);

        url=getString(R.string.ws_server_url);

        drawerView=(View)findViewById(R.id.drawerView);
        drawerLayout.addDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        stompClient= Stomp.over(Stomp.ConnectionProvider.OKHTTP,url);
        roomDB=RoomDB.getInstance(getApplicationContext());
        dao=roomDB.getRoomInfoDao();

        headerList=new ArrayList<>();
        headerList.add(new StompHeader("jwt",dao.SelectUserInfoJwt())); //header에 jwt담아서 커넥트하기
        //Log.d("MyJwt",dao.SelectUserInfoJwt());

        nickName=getIntent().getExtras().getString("nickName");
        roomName=getIntent().getExtras().getString("roomName");
        professorName=getIntent().getExtras().getString("professorName");
        titleTextView.setText(roomName); //타이틀바 과목명
        subjectTextView.setText(roomName); //목록 과목명
        professorTextView.setText(professorName+" 교수님");
        studentId=dao.SelectUserInfoStudentId();
        myNickName.setText(nickName); //리스트 내 닉네임
        roomId=getIntent().getExtras().getInt("roomId");
        roomId2=String.valueOf(roomId);

        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(roomId);
        //notificationManager.cancelAll();

        position=dao.SelectRoomInfoPosition(roomId);
        noti=dao.SelectRoomInfoNoti2(roomId);

        //채팅 리사이클러뷰
        adapter=ChatAdapter.getInstance();

        adapter.setInflater(getLayoutInflater());
        chatInfos=dao.SelectChatInfo(roomId);
        manager=new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
        adapter.setChatInfos(chatInfos);

        //유저리스트 리사이클러뷰
        roomInNames=dao.SelectRoomInNames2(roomId);
        userListAdapter=new UserListAdapter(getLayoutInflater());
        userListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userListRecyclerView.setAdapter(userListAdapter);
        userListAdapter.setItems(roomInNames,nickName);

        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                showReportDialog(position);
            }
        });

        if(chatInfos.size()>0){ //이전 채팅 내용 위치로 가기
            recyclerView.scrollToPosition(position);
            Toast.makeText(getApplicationContext(), "여기까지 읽었습니다.", Toast.LENGTH_SHORT).show();
        }

        if(noti==1){
            notiBtn.setBackgroundResource(R.drawable.ic_baseline_notifications_24);
        }else{
            notiBtn.setBackgroundResource(R.drawable.ic_baseline_notifications_off_24);
        }

        connectivityManager = getSystemService(ConnectivityManager.class);
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {

                initStomp();

                //서버에 정지나 새학기인지 확인하기 위해 데이터 전송
                adapter.connect=roomId;
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("roomId",roomId2);
                    jsonObject.put("studentId",studentId);
                    jsonObject.put("token",dao.SelectUserInfoToken());
                    jsonObject.put("version",VERSION);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                stompClient.send("/pub/ay/connectchat",jsonObject.toString()).subscribe();

            }

            @Override
            public void onLost(Network network) {
                adapter.connect=-1;
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stompClient.isConnected()){
                    String msg=messageEdit.getText().toString();
                    if(msg.equals(""))return;
                    ChatInfo chatInfo=new ChatInfo(roomId,nickName,getTime(),msg,sendMsgSignal);
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("roomId",roomId2);
                        jsonObject.put("studentId",studentId);
                        jsonObject.put("nickName",nickName);
                        jsonObject.put("content",msg);
                        jsonObject.put("time",getTime());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    stompClient.send("/pub/ay/chat",jsonObject.toString()).subscribe();
                    dao.InsertChatInfo(chatInfo);
                    adapter.addChatInfos(chatInfo);
                    messageEdit.setText("");
                    recyclerView.scrollToPosition(chatInfos.size()-1);

                }
            }
        });

        notiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noti==0){
                    notiBtn.setBackgroundResource(R.drawable.ic_baseline_notifications_24);
                    noti=1;
                    dao.UpdateRoomInfoNoti(noti,roomId);
                }else{
                    notiBtn.setBackgroundResource(R.drawable.ic_baseline_notifications_off_24);
                    noti=0;
                    dao.UpdateRoomInfoNoti(noti,roomId);
                }
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personTextView.setText(String.valueOf(roomInNames.size()));
                drawerLayout.openDrawer(drawerView);
            }
        });

    }

    public void initStomp(){
        if(stompClient.isConnected())return;
        stompClient.connect(headerList);
        //stompClient.connect();



        stompClient.topic("/sub/chat/"+roomId2).subscribe(topicMessage->{
        JsonParser parser=new JsonParser();
        JsonObject jsonObject=(JsonObject) parser.parse(topicMessage.getPayload());
       // Log.d("receive Msg", jsonObject.toString());


        if(jsonObject.get("start")!=null){ //웹소켓 연결한 회원
            //Log.d("server start","good");
            if(jsonObject.get("start").getAsInt()==doubleLoginSignal){ //이중로그인이라서 로그아웃 시킨다.
                //Log.d("dobuleLogin","bye");
                if(jsonObject.get("studentId").getAsString().equals(studentId)) { //이중로그인 유저였다면?
                    dao.DeleteAllRoomInfos();
                    dao.DeleteAllRoomInNames();
                    dao.DeleteChatInfos();
                    dao.UpdateUserInfoAutoLogin(0);
                    Intent intent=new Intent(ChatActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }else if(jsonObject.get("start").getAsInt()==suspendedSignal) { //정지 회원
                if(jsonObject.get("studentId").getAsString().equals(studentId)){ //정지회원의 학번이 나였다면?
                    //Log.d("suspended","bye");
                    dao.DeleteAllRoomInfos();
                    dao.DeleteAllRoomInNames();
                    dao.DeleteChatInfos();
                    dao.UpdateUserInfoAutoLogin(0);
                    //dao.UpdateUserInfoSuspendedDate(jsonObject.get("suspendedDate").getAsString()); //db 정지날짜 주기
                    Intent intent=new Intent(ChatActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }else if(jsonObject.get("start").getAsInt()==semeterSignal){ //새학기 시작이라 로그아웃 시킨다.
                //Log.d("새학기시작","bye");
                dao.DeleteAllRoomInfos();
                dao.DeleteAllRoomInNames();
                dao.DeleteChatInfos();
                dao.UpdateUserInfoAutoLogin(0);
                Intent intent=new Intent(ChatActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else{
                //Log.d("update","bye");
                //플레이스토어 띄우자.
            }
        }else{ //채팅메시지
            String nickName=jsonObject.get("nickName").getAsString();
            if(jsonObject.get("pmType").getAsInt()==1){ //pc에서 보냄
                if(!nickName.equals(this.nickName)) { //상대방이 보낸 메시지
                    ChatInfo chatInfo = new ChatInfo(roomId, nickName,
                            jsonObject.get("time").getAsString(), jsonObject.get("content").getAsString(), receiveMsgSignal);
                    dao.InsertChatInfo(chatInfo);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addChatInfos(chatInfo);
                        }
                    });
                }else{ //내가 보낸 메시지
                    ChatInfo chatInfo = new ChatInfo(roomId, nickName,
                            jsonObject.get("time").getAsString(), jsonObject.get("content").getAsString(), sendMsgSignal);
                    dao.InsertChatInfo(chatInfo);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addChatInfos(chatInfo);
                        }
                    });
                }
            }else { //모바일에서 보냄
                if (!nickName.equals(this.nickName)) { //상대방이 보낸 메시지
                    int type=jsonObject.get("type").getAsInt();
                    ChatInfo chatInfo;
                    if(type==0){//입장
                        chatInfo=new ChatInfo(roomId
                                ,"",null,nickName+" 입장", 1);
                        RoomInName roomInName=new RoomInName(roomId,nickName);
                        dao.InsertRoomInName(roomInName);
                        roomInNames.add(nickName);
                    }else if(type==1){//퇴장
                        chatInfo=new ChatInfo(roomId
                                ,"",null,nickName+" 퇴장", 1);
                        dao.DeleteRoomInName(roomId,nickName);
                        roomInNames.remove(nickName);
                    }else{ //메시지 수신
                        chatInfo = new ChatInfo(roomId, nickName,
                                jsonObject.get("time").getAsString(), jsonObject.get("content").getAsString(), receiveMsgSignal);
                    }

                    dao.InsertChatInfo(chatInfo);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addChatInfos(chatInfo);
                            if(manager.findLastVisibleItemPosition()==(chatInfos.size()-2)){
                                recyclerView.scrollToPosition(chatInfos.size()-1);
                            }else{
                                Toast.makeText(getApplicationContext(),"새 메시지가 도착했습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    });


    }



    public void showReportDialog(int position){
        reportDialog.show();
        TextView reportName= reportDialog.findViewById(R.id.reportName);
        TextView reportContent= reportDialog.findViewById(R.id.reportContent);
        EditText reportWhy=reportDialog.findViewById(R.id.reportWhy);
        reportName.setText(chatInfos.get(position).getName());
        reportContent.setText(chatInfos.get(position).getContent());
        reportName.setText(chatInfos.get(position).getName());
        Button reportCancel=reportDialog.findViewById(R.id.reportCancel);
        reportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportDialog.dismiss();
            }
        });

        Button reportSend= reportDialog.findViewById(R.id.reportSend);
        reportSend.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("reportName",chatInfos.get(position).getName());
                    jsonObject.put("reportContent",chatInfos.get(position).getContent());
                    jsonObject.put("reportWhy",reportWhy.getText().toString());
                    jsonObject.put("reporter",nickName);
                    jsonObject.put("studentId",dao.SelectUserInfoStudentId());
                    jsonObject.put("roomName",roomName);
                    jsonObject.put("professorName",professorName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stompClient.send("/pub/ay/report",jsonObject.toString()).subscribe();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"신고 완료: "+reportWhy.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
                reportDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_chat,menu);
        if(noti==0){
            menu.getItem(0).setIcon(R.drawable.ic_baseline_notifications_off_24);
        }else{
            menu.getItem(0).setIcon(R.drawable.ic_baseline_notifications_24);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_noti:
                if(noti==0){ //알림이미지 변경
                    noti=1;
                    dao.UpdateRoomInfoNoti(noti,roomId);
                    item.setIcon(R.drawable.ic_baseline_notifications_24);
                }else{
                    noti=0;
                    dao.UpdateRoomInfoNoti(noti,roomId);
                    item.setIcon(R.drawable.ic_baseline_notifications_off_24);
                }
                break;
            case R.id.menu_userlist:
                drawerLayout.openDrawer(drawerView);
                //userListAdapter.setItems(roomInNames,nickName);
                return true;
        }
        return false;
    }

    DrawerLayout.DrawerListener listener=new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    private String getTime() {
        long now = System.currentTimeMillis();
        TimeZone tz=TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm", Locale.KOREA);
        dateFormat.setTimeZone(tz);
        String getTime = dateFormat.format(date);
        String hh=String.valueOf(getTime.charAt(0))+String.valueOf(getTime.charAt(1));
        String mm=String.valueOf(getTime.charAt(2))+String.valueOf(getTime.charAt(3));

        int h=Integer.parseInt(hh);
        StringBuilder sb=new StringBuilder();
        if(h>12){
            sb.append("오후 ");
            sb.append(String.valueOf(h-12));
            sb.append(":");
            sb.append(mm);
            return sb.toString();
        }else if(h==12){
            sb.append("오후 ");
            sb.append(hh);
            sb.append(":");
            sb.append(mm);
            return sb.toString();
        }else {
            sb.append("오전 ");
            sb.append(hh);
            sb.append(":");
            sb.append(mm);
            return sb.toString();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        stompClient.disconnect();
        adapter.connect=-1;
        dao.UpdateRoomInfoPosition(chatInfos.size()-1,roomId); //이전 채팅 데이터 기록
    }

    @Override
    protected void onDestroy() {
        adapter.initInstance();
        super.onDestroy();
    }
}