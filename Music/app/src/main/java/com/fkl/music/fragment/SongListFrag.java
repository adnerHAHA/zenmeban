package com.fkl.music.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fkl.music.MusicPlayer;
import com.fkl.music.R;
import com.fkl.music.adapter.MusicAdapter;
import com.fkl.music.bean.MusicInfo;
import com.fkl.music.utils.MusicList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2016/10/12.
 */
public class SongListFrag extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listSong;
    private List<MusicInfo> mifos=new ArrayList<MusicInfo>();
    ;
    private MusicAdapter adapter;
    private List<String> url;
    private int[] sid;
    private String[] songName;
    private String[] singerName;
    private String[] songURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.song_list, container, false);
        listSong = (ListView) inflate.findViewById(R.id.lv_song);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inidata();

        //adapter.notify();
    }

    private void inidata() {
        adapter = new MusicAdapter(getActivity(), mifos);
        listSong.setAdapter(adapter);

        // mifos = MusicList.getMusicData(getActivity());
        mifos.addAll(MusicList.getMusicData(getActivity()));
        // hd.sendEmptyMessage(100);
        adapter.notifyDataSetChanged();
        listSong.setOnItemClickListener(this);



    }

    /*Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("---", "页面刷新完毕: ");


        }
    };*/


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        playMusic(position);


    }

    private void playMusic(int position) {
        Intent intent = new Intent(getActivity(), MusicPlayer.class);
        intent.putExtra("position", position);
        getActivity().startActivity(intent);
    }
}
