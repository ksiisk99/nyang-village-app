package com.uni.aychat.fcm;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uni.aychat.R;
import com.uni.aychat.activity.MainActivity;
import com.uni.aychat.adapter.ChatAdapter;
import com.uni.aychat.db.entity.ChatInfo;
import com.uni.aychat.db.entity.NotiRoomInfo;
import com.uni.aychat.db.RoomDB;
import com.uni.aychat.db.entity.RoomInName;
import com.uni.aychat.db.RoomInfoDao;
import com.uni.aychat.db.entity.UserInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FcmService extends FirebaseMessagingService{

    private static final String TAG="fcm";
    RoomDB roomDB=RoomDB.getInstance();
    RoomInfoDao roomInfoDao=roomDB.getRoomInfoDao();


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + message.getFrom());
//
        int roomId=Integer.parseInt(message.getData().get("roomId"));
        if(ChatAdapter.isConnect()==roomId)return; //해당하는 채팅방에 대해서만 fcm을 받으면 안된다.

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + message.getData());
            String nickName=message.getData().get("nickName");
            int type=Integer.parseInt(message.getData().get("type"));

            switch(type){
                case 0:
                    RoomInName roomInName=new RoomInName(roomId,message.getData().get("nickName"));
                    ChatInfo chatInfo=new ChatInfo(roomId
                            ,"",null,message.getData().get("nickName")+" 입장",type);
                    roomInfoDao.InsertRoomInName(roomInName);
                    roomInfoDao.InsertChatInfo(chatInfo);

                    break;
                case 1:
                    ChatInfo chatInfo2=new ChatInfo(roomId
                            ,"",null,message.getData().get("nickName")+" 퇴장",type);
                    roomInfoDao.DeleteRoomInName(roomId,message.getData().get("nickName"));
                    roomInfoDao.InsertChatInfo(chatInfo2);
                    break;
                case 2:
                    //Log.d("nickName",message.getData().get("nickName"));
                    String content=message.getData().get("content");

                    ChatInfo chatInfo3;
                    if(Integer.parseInt(message.getData().get("pmType"))==1){
                        if(nickName.equals(roomInfoDao.SelectRoomInfoInNickName(roomId))){ //웹에서 보내는 메시지일 경우 내가 보낸 것으로 처리해야한다.
                            chatInfo3=new ChatInfo(roomId,
                                    message.getData().get("nickName"),message.getData().get("time"),message.getData().get("content"),3);
                        }else{
                            chatInfo3=new ChatInfo(roomId,
                                    message.getData().get("nickName"),message.getData().get("time"),message.getData().get("content"),type);
                        }
                    }else{
                        chatInfo3=new ChatInfo(roomId,
                                message.getData().get("nickName"),message.getData().get("time"),message.getData().get("content"),type);
                    }


                    roomInfoDao.InsertChatInfo(chatInfo3);

                    NotiRoomInfo notiRoomInfo=roomInfoDao.SelectRoomInfoNoti(roomId);
                    if(notiRoomInfo==null)break;
                    if(notiRoomInfo.getNoti()==1){ //알림
                        try {
                            sendNotification(nickName,content,notiRoomInfo.getRoomName(),roomId);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
            }


        }

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + message.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String nickName, String content,String roomName,int roomId) throws UnsupportedEncodingException {

        //알림 눌렀을때 어떤 액티비티 화면으로 갈 것인지 설정
        Intent intent=new Intent(FcmService.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("notiClick",1);
        intent.putExtra("roomId",roomId);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,roomId,
                intent,PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder=
                new NotificationCompat.Builder(this,roomName)
                .setSmallIcon(R.drawable.ic_stat_noti_icon)
                .setColor(getColor(R.color.icon_color))
                .setContentTitle(URLDecoder.decode(roomName,"UTF-8"))
                .setContentText(URLDecoder.decode(nickName+": "+content,"UTF-8"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup("AY_GROUP");
        NotificationCompat.Builder summary=new NotificationCompat.Builder(this,roomName)
                .setContentTitle(URLDecoder.decode(roomName,"UTF-8"))
                .setSmallIcon(R.drawable.ic_stat_noti_icon)
                .setColor(getColor(R.color.icon_color))
                .setAutoCancel(true)
                .setGroup("AY_GROUP")
                .setGroupSummary(true);
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel(roomName,roomName,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("push");

        notificationManager.createNotificationChannel(channel);

        //알림 표시
        notificationManager.notify(roomId,notificationBuilder.build());
        notificationManager.notify(0,summary.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server..onNewToken(token);
        //Log.d("NewToken",token);
        if(roomInfoDao.SelectUserInfo()==null)//최초 로그인
            roomInfoDao.InsertUserInfo(new UserInfo("1",token,null,0,null));
        else
            roomInfoDao.UpdateUserInfoToken(token); //토큰 변경
    }
}
