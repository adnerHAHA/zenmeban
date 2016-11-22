package com.fkl.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fkl.music.bean.MusicInfo;
import com.fkl.music.service.LocalMusicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {
    private ImageButton btnLast,btnPlay,btnNext;
    private TextView tvSongName;
    private int flag=1;
    private static final int STATE_PLAY = 1;// 播放状态设为1,表示播放状态
    private static final int STATE_PAUSE = 2;// 播放状态设为2，表示暂停状态
    private static final int PLAY = 1;// 定义播放状态
    private static final int PAUSE = 2;// 暂停状态
    private static final int STOP = 3;// 停止
    private int position=-1;
    String [] songName=null;
    String[] songURL=null;
    private static final String MUSIC_CURRENT = "com.music.currentTime";
    private static final String MUSIC_DURATION = "com.music.duration";
    private static final String MUSIC_NEXT = "com.music.next";
    private static final String MUSIC_UPDATE = "com.music.update";
   private MediaPlayer mplayer=null;
    private SeekBar seekBar;
    boolean seekbarRecycle = true;
    private int[] _ids;
    boolean pauseOrNot=false;
    private List<MusicInfo> musics=new ArrayList<MusicInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        inidata();
        intitView();
    }
    private void inidata() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        position=bundle.getInt("position");
        songName=bundle.getStringArray("songName");
        songURL=bundle.getStringArray("songURL");

    }

    private void intitView() {
        btnLast= (ImageButton) findViewById(R.id.btn_last);
        btnPlay= (ImageButton) findViewById(R.id.btn_play);
        btnNext= (ImageButton) findViewById(R.id.btn_next);
        tvSongName= (TextView) findViewById(R.id.tv_songName);
        seekBar= (SeekBar) findViewById(R.id.seekbar);
        hd.sendEmptyMessage(200);
       // showTextSongName();
        changeSeekProgress();
        showPlaybtn();
        showLastBtn();
        showNextBtn();
        showSongName();


    }

    private void showSongName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MusicInfo info = musics.get(LocalMusicService.playIndex);
                tvSongName.setText(info.getTitle());

            }
        }).start();
    }


    private void changeSeekProgress() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 if (fromUser){
                    mplayer.seekTo(progress);
                     seekBar.setProgress(progress);
                 }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                paurse();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                play();

            }
        });
    }


    private void showPlaybtn() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case STATE_PLAY:
                        paurse();
                        break;
                    case  STATE_PAUSE:
                        play();
                        break;
                }
            }
        });
    }
    private void showLastBtn() {
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastOne();
            }
        });

    }
    private void showNextBtn() {
      btnNext.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              nextOne();
          }
      });


    }

    private void nextOne() {
        if(position==songURL.length-1){
            position=0;
        }else if (position<songURL.length-1){
            position++;
        }
        seekbarRecycle=false;
        stop();
        hd.sendEmptyMessage(200);
        play();
    }

    private void stop() {
        if (mplayer!=null&&mplayer.isPlaying()){
            mplayer.stop();
            seekbarRecycle=false;


        }
    }


    private void lastOne() {
        if(position==0){
            position=songURL.length-1;
        }else if (position>0){
            position--;
        }
        seekbarRecycle=false;
        stop();
        hd.sendEmptyMessage(200);
        play();

    }


    private void play() {
        seekbarRecycle=true;
        flag=STATE_PLAY;
        mplayer=new MediaPlayer();
        try {
            mplayer.setDataSource(songURL[position]);
            mplayer.prepare();
            mplayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (position==songURL.length){
                    position=0;
                }
                seekbarRecycle=false;
                play();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    hd.sendEmptyMessage(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
Handler hd=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 100:
                seekBar.setMax(mplayer.getDuration());
                seekBar.setProgress(mplayer.getCurrentPosition());
                break;
            case 200:

                tvSongName.setText(songName[position]);
                break;


        }

    }
};

    private void paurse() {
        flag=STATE_PAUSE;
       if (mplayer!=null&&mplayer.isPlaying()){
           mplayer.pause();
       }

    }


}
