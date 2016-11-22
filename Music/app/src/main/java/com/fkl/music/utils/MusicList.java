package com.fkl.music.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.fkl.music.bean.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adner on 2016/10/11.
 */
public class MusicList {
static  List<MusicInfo> infos = new ArrayList<MusicInfo>();

    public static List<MusicInfo> getMusicData(Context ctx) {

        MusicInfo info = null;



        ContentResolver cr = ctx.getContentResolver();
        if (null != cr) {
            Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (null == cursor) {
                return null;
            }
            if (cursor.moveToFirst()) {
                do {
                    int _id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    if ("<unkown>".equals(singer)) {
                        singer = "未知歌手";
                    }
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                    long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String sbr = name.substring(name.length() - 3, name.length());
                    Log.e("-----", url);
                    info = new MusicInfo();
                    info.setSid(_id);
                    info.setTitle(title);
                    info.setSinger(singer);
                    info.setAlbum(album);
                    info.setSize(size);
                    info.setTime(time);
                    info.setUrl(url);
                    info.setName(name);
                    info.setSbr(sbr);
                    infos.add(info);

                } while (cursor.moveToNext());


            }
        }
        return infos;

    }
    public static List<MusicInfo> getMusics(){
        return infos;
    }
}




