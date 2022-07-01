package com.uni.aychat.db;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.uni.aychat.db.entity.ChatInfo;
import com.uni.aychat.db.entity.RoomInName;
import com.uni.aychat.db.entity.RoomInfo;
import com.uni.aychat.db.entity.UserInfo;

@Database(entities = {RoomInfo.class, UserInfo.class, RoomInName.class, ChatInfo.class}, version=22,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database=null;
    private static String DB_NAME="AY";

    public synchronized static RoomDB getInstance(Context context){
        if(database==null){
            database= Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public static RoomDB getInstance(){
        return database;
    }

    public abstract RoomInfoDao getRoomInfoDao();

    public static void destroyInstance(){database=null;}

    public class MyApplication extends Application{
        @Override
        public void onCreate() {
            super.onCreate();
            RoomDB.getInstance(this);
        }
    }
}
