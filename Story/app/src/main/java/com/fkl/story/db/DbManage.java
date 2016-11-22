package com.fkl.story.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adner on 2016/10/30.
 */
public class DbManage {
    private DBHelper helper;

    public DbManage(Context context) {
        helper = new DBHelper(context);

    }

    public long add(ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long insert = -1;
        try {
            insert = db.insert("story", null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return insert;

    }

    public Cursor query(String selection, String[] selectionArgs) {
        Cursor ret = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        ret = db.query("story", null, selection, selectionArgs, null, null,
                null);
        return ret;
    }
}
