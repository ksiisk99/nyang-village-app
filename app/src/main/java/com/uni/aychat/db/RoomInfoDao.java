package com.uni.aychat.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.uni.aychat.db.entity.ChatInfo;
import com.uni.aychat.db.entity.NotiRoomInfo;
import com.uni.aychat.db.entity.RoomInName;
import com.uni.aychat.db.entity.RoomInfo;
import com.uni.aychat.db.entity.UserInfo;

import java.util.List;


@Dao
public interface RoomInfoDao {

    //RoomInfo
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertRoomInfo(RoomInfo roomInfo); //방 정보 저장

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertRoomInfos(List<RoomInfo> roomInfos); //서버에서 받아온 방 정보 리스트 저장

    @Query("DELETE FROM RoomInfo  WHERE roomId NOT IN (:roomIds)")
    void DeleteRoomInfos(List<Integer> roomIds); //수강과목이 바뀌면 기존 과목은 삭제

    @Query("SELECT * FROM RoomInfo")
    List<RoomInfo> SelectRoomInfos(); //방 정보 불러오기

    @Query("DELETE FROM RoomInfo")
    void DeleteAllRoomInfos(); //방정보 전체 삭제

    @Query("SELECT nickName FROM RoomInfo WHERE roomId=:roomId") //웹에서 채팅보낼시 해당 기기에 내가 보낸 것으로 해야함
    String SelectRoomInfoInNickName(int roomId);

    @Query("SELECT position FROM RoomInfo WHERE roomId=:roomId") //마지막으로 읽은 채팅
    int SelectRoomInfoPosition(int roomId);

    @Query("UPDATE RoomInfo SET position=:position WHERE roomId=:roomId") //마지막으로 읽은 채팅위치 저장
    void UpdateRoomInfoPosition(int position,int roomId);

    @Query("SELECT roomName, noti FROM RoomInfo WHERE roomId=:roomId")
    NotiRoomInfo SelectRoomInfoNoti(int roomId); //fcm 알림

    @Query("SELECT noti FROM RoomInfo WHERE roomId=:roomId")
    int SelectRoomInfoNoti2(int roomId); //fcm알림

    @Query("UPDATE RoomInfo SET noti=:noti WHERE roomId=:roomId")
    void UpdateRoomInfoNoti(int noti,int roomId); //알림 해제/허용


    //UserInfo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertUserInfo(UserInfo userInfo); //유저정보저장

    @Query("DELETE FROM UserInfo")
    void DeleteUserInfo(); //유저정보삭제

    @Query("SELECT * FROM UserInfo LIMIT 1")
    UserInfo SelectUserInfo(); //유저정보 가져오기

    @Query("SELECT studentId FROM UserInfo LIMIT 1") //학번 가져오기
    String SelectUserInfoStudentId();

    @Query("UPDATE UserInfo SET suspendedDate=:suspendedDate") //정지 풀리는 날짜
    void UpdateUserInfoSuspendedDate(String suspendedDate);

    @Query("UPDATE UserInfo SET jwt=:jwt")//jwt 교체
    void UpdateUserInfoJwt(String jwt);

    @Query("SELECT suspendedDate FROM UserInfo LIMIT 1") //정지 날짜 가져오기
    String SelectUserInfoSuspendedDate();

    @Query("SELECT token FROM UserInfo LIMIT 1") //토큰 가져오기
    String SelectUserInfoToken();

    @Query("UPDATE UserInfo SET token=:token") //토큰 변경
    void UpdateUserInfoToken(String token);

    @Query("SELECT autoLogin FROM UserInfo LIMIT 1") //자동로그인 여부 가져오기
    int SelectUserInfoAutoLogin();

    @Query("UPDATE UserInfo SET autoLogin=:autoLogin")//자동로그인 변경
    void UpdateUserInfoAutoLogin(int autoLogin);

    @Query("SELECT COUNT(*) FROM UserInfo")
    int SelectUserInfoCount();

    @Query("UPDATE UserInfo SET studentId=:studentId")//아이디 변경
    void UpdateUserInfoStudentId(String studentId);

    @Query("SELECT jwt FROM UserInfo LIMIT 1") //jwt 가져오기
    String SelectUserInfoJwt();


    //RoomInName
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertRoomInNames(List<RoomInName> roomInNames); //방 안에 사용자 닉네임 저장

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertRoomInName(RoomInName roomInName); //입장할때마다 사용자이름 추가

    @Query("DELETE FROM RoomInName WHERE roomId=:roomId AND name=:name")
    void DeleteRoomInName(int roomId, String name); //퇴장할때 사용자이름 제거

    @Query("DELETE FROM RoomInName WHERE roomId NOT IN (:roomIds)")
    void DeleteRoomInNames(List<Integer> roomIds); //수강과목이 바뀌면 기존 과목은 삭제

    @Query("SELECT * FROM RoomInName")
    List<RoomInName> SelectRoomInNames();

    @Query("DELETE FROM RoomInName")
    void DeleteAllRoomInNames(); //방 안에 사용자 닉네임 전체 삭제

    @Query("SELECT name FROM RoomInName WHERE roomId=:roomId")
    List<String> SelectRoomInNames2(int roomId); //방 안에 사용자 닉네임 불러오기



    //ChatInfo
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertChatInfo(ChatInfo chatInfo); //채팅 내용 저장

    @Query("DELETE FROM ChatInfo WHERE roomId NOT IN (:roomIds)")
    void DeleteChatInfos(List<Integer> roomIds); //수강과목이 바뀌면 기존 과목의 채팅 내용은 삭제

    @Query("DELETE FROM ChatInfo")
    void DeleteChatInfos(); //채팅 내용 전체 삭제

    @Query("SELECT * FROM ChatInfo WHERE roomId=:roomId")
    List<ChatInfo> SelectChatInfo(int roomId); //채팅 내용 가져오기

    @Query("DELETE FROM ChatInfo WHERE roomId=:roomId")
    void DeleteChatInfo(int roomId); //정정때 수강과목 바뀌면 기존 채팅 내용 삭제

}
