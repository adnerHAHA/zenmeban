package com.fkl.story.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adner on 2016/10/29.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String  DBNNAME="story.db";
    public static  String sql="create table story(_id integer primary key autoincrement,sid varchar(64)," +
            "story_info varchar(64),story_time Long,uid varchar(64),city varchar(64))";
    public DBHelper(Context context) {
        super(context, DBNNAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
               db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
