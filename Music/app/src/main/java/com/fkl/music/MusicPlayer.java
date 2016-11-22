package com.fkl.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fkl.music.bean.MusicInfo;
import com.fkl.music.service.LocalMusicService;
import com.fkl.music.utils.LrcView;
import com.fkl.music.utils.MusicList;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnLast, btnPlay, btnNext, btnP;
    public static LrcView lrcView;
    private TextView tvSongName;
    int position=0;
    private SeekBar seekBar;
    private LocalMusicService mservice = null;
    MyServiceConn myServiceConn;
    int index=0;
    private static List<Integer> lyricTime = new ArrayList<>();
    private List<String> lrcs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        inidata();
        intitView();

    }

    private void inidata() {

        Intent intent = this.getIntent();
        position = intent.getIntExtra("position", 0);
        Intent mintent = new Intent(MusicPlayer.this, LocalMusicService.class);
        mintent.putExtra("op", "play");
        mintent.putExtra("position", position);
        startService(mintent);
        myServiceConn = new MyServiceConn();
        Intent bindIntent = new Intent(MusicPlayer.this, LocalMusicService.class);
        bindService(bindIntent, myServiceConn, BIND_AUTO_CREATE);
        List<MusicInfo> data = MusicList.getMusicData(MusicPlayer.this);
        index=LocalMusicService.playIndex;
    }

    private void intitView() {
        btnLast = (ImageButton) findViewById(R.id.btn_last);
        btnPlay = (ImageButton) findViewById(R.id.btn_play);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        lrcView= (LrcView) findViewById(R.id.lrc);
        tvSongName = (TextView) findViewById(R.id.tv_songName);
        btnP= (ImageButton) findViewById(R.id.btnp);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        btnLast.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnP.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new ChangeProgress());
        showSongName();
        //showLrc();


    }

    private void showLrc() {
        new Thread(new Runnable() {
            @Override
            public void run() {

             while (true){
                 try {
                     Thread.sleep(2000);
                     hd.sendEmptyMessage(200);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }

            }

        }).start();
    }


    private void showSongName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
              hd.sendEmptyMessage(100);
            }
        }).start();
    }

Handler hd=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 100:
                Intent intent = getIntent();
                String title = intent.getStringExtra("songName");
                tvSongName.setText(title);

                break;
            case 200:

                //lrcView.setText(lrcs.toString());
                lrcView.setSententceEntities(lrcs);
                lrcView.refreshDrawableState();

                break;

        }
    }
};
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MusicPlayer.this, LocalMusicService.class);
        switch (v.getId()) {
            case R.id.btn_play:
                position=LocalMusicService.playIndex;
                btnPlay.setImageResource(R.mipmap.landscape_player_btn_pause_press);
                btnP.setImageResource(R.mipmap.landscape_player_btn_pause_normal);
                intent.putExtra("op", "play");
                intent.putExtra("position", position);

                break;
            case R.id.btnp:
                btnP.setImageResource(R.mipmap.landscape_player_btn_pause_press);
                btnPlay.setImageResource(R.mipmap.landscape_player_btn_play_normal);
                intent.putExtra("op", "pu");
                break;
            case R.id.btn_last://上一曲
                btnLast.setImageResource(R.mipmap.landscape_player_btn_pre_press);
                btnPlay.setImageResource(R.mipmap.landscape_player_btn_pause_press);
                intent.putExtra("op", "last");
                break;

            case R.id.btn_next://下一曲
                btnNext.setImageResource(R.mipmap.landscape_player_btn_next_press);
                btnPlay.setImageResource(R.mipmap.landscape_player_btn_pause_press);
                intent.putExtra("op", "next");
                break;
        }
        startService(intent);

    }

    public interface CallMediaChangeCallBack {
        public void changeNew(int progress);

        public void pauseNew();

        public void startNew();
    }

    CallMediaChangeCallBack callMediaChangeCallBack = null;

    /**
     * 进度条监听
     */
    private class ChangeProgress implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                seekBar.setProgress(progress);
                callMediaChangeCallBack.changeNew(progress);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            callMediaChangeCallBack.pauseNew();

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            callMediaChangeCallBack.startNew();

        }
    }

    private class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mservice = ((LocalMusicService.MyBinder) binder).getService();
            mservice.setProgressListener(new ProgressListener());
            if (mservice instanceof CallMediaChangeCallBack) {
                callMediaChangeCallBack = mservice;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mservice = null;

        }
    }

    private class ProgressListener implements LocalMusicService.MyCallbackSeekBar {

        @Override
        public void refresh(int currentPosition, int maxPosition) {
            seekBar.setMax(maxPosition);
            seekBar.setProgress(currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myServiceConn);
    }
}




