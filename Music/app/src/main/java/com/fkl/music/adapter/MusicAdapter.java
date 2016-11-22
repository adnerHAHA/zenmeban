package com.fkl.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fkl.music.R;
import com.fkl.music.bean.MusicInfo;

import java.util.List;

/**
 * Created by adner on 2016/10/11.
 */
public class MusicAdapter extends BaseAdapter {
    private Context ctx;
    private List<MusicInfo> infos;

    public MusicAdapter(Context ctx,List<MusicInfo> infos) {
        this.ctx = ctx;
        this.infos=infos;
    }

    @Override
    public int getCount() {
        return infos==null?0: infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView hd;
        if(convertView==null){
            hd = new HoldView();
            convertView= LayoutInflater.from(ctx).inflate(R.layout.music_info,parent,false);
            hd.tv_song= (TextView) convertView.findViewById(R.id.tv_song);
            hd.tv_singer= (TextView) convertView.findViewById(R.id.tv_singer);
            convertView.setTag(hd);


        }else{
            hd= (HoldView) convertView.getTag();
        }
        MusicInfo info=infos.get(position);
        hd.tv_song.setText(info.getTitle());
        hd.tv_singer.setText(info.getSinger());

        return convertView;
    }
    private class  HoldView{
        public TextView tv_song;
        public  TextView tv_singer;
    }
}
