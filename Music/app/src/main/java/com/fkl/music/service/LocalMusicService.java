package com.fkl.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.fkl.music.MusicPlayer;
import com.fkl.music.R;
import com.fkl.music.bean.MusicInfo;
import com.fkl.music.utils.LrcRead;
import com.fkl.music.utils.LrcUtils;
import com.fkl.music.utils.MusicList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalMusicService extends Service implements MusicPlayer.CallMediaChangeCallBack {
    public static int _id = 1;
    private List<MusicInfo> musics=new ArrayList<MusicInfo>();
    private MediaPlayer player;
    private boolean isPlay = false;
    private boolean isPaurse = false;
    public  static int playIndex=0;
    private boolean recycleTag=true;
    private LrcUtils lrc;
    private MyBinder myBinder = new MyBinder();
    private  List<LrcUtils.LrcContent> lrcslist=new ArrayList<>();
    private List<String> lrcLists=new ArrayList<>();
    private List<Integer> time=new ArrayList<>();
    private int index = 0;
    // 初始化歌曲播放时间的变量
    private int CurrentTime = 0;
    // 初始化歌曲总时间的变量
    private int CountTime = 0;

    @Override
    public void changeNew(int progress) {
        player.seekTo(progress);

    }

    @Override
    public void pauseNew() {
        player.pause();

    }

    @Override
    public void startNew() {
        player.start();

    }



    public class MyBinder extends Binder {
        public  LocalMusicService getService() {
            return LocalMusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public LocalMusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musics.addAll(MusicList.getMusicData(LocalMusicService.this));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String op = intent.getStringExtra("op");

        if (op.equals("play")) {
            int position = intent.getIntExtra("position", -1);
            if (position!=playIndex){
                isPlay=false;
                isPaurse=false;
                initPlay();
            }

            if(position!=-1){
                playIndex=position;
                play();
            }

        } else if (op.equals("pu")) {
            paurse();
        } else if (op.equals("last")) {
            last();
        } else if (op.equals("next")) {
            next();
        } else if (op.equals("stop")) {
            stop();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initPlay() {
        try {
            player.stop();
            player.release();
            player=null;
        }catch (Exception err){
            player=null;
        }
    }


    private void play() {//播放音乐
        time=LrcRead.getLyricTime();
        Log.e("------", time+"");
        lrcLists=LrcRead.readLrc(musics.get(playIndex).getUrl());
        //lrcLists.addAll(LrcRead.readLrc(musics.get(playIndex).getUrl()));
        MusicPlayer.lrcView.setSententceEntities(lrcLists);
       MusicPlayer.lrcView.setAnimation(AnimationUtils.loadAnimation(LocalMusicService.this, R.anim.alpha_z));
        //mHandler.post(mRunnable);
        if(!isPaurse&&!isPlay){
            player=new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                player.setDataSource(musics.get(playIndex).getUrl());
                player.prepare();
                player.start();
                isPlay=true;
                recycleTag=true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (recycleTag) {
                            try {
                                Thread.sleep(1000);
                                myCallbackSeekBar.refresh(player.getCurrentPosition(), player.getDuration());
                                //MusicPlayer.lrcView.SetIndex(player.getCurrentPosition());
                                for(int i=0;i<lrcLists.size();i++){
                                    if (LrcRead.getLyricTime().get(i)<=player.getCurrentPosition()&&LrcRead.getLyricTime().get(i+1)>player.getCurrentPosition()){
                                        MusicPlayer.lrcView.SetIndex(i);
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (Exception err) {
                                Log.e("----", "线程结束");
                            }
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(isPlay&&isPaurse){
            player.start();
        }
        player.setOnCompletionListener(new PlayerLisener());


    }

    private void paurse() {//暂停
        if (null!=player&&player.isPlaying()){
            player.pause();
            isPaurse=true;
        }


    }

    private void stop() {//停止
        if (null!=player&&player.isPlaying()){
            initPlay();
            isPaurse=false;
            isPlay=false;
            recycleTag=false;
        }

    }

    private void last() {//上一首
        stop();
        if (playIndex==0){
            playIndex=musics.size();
        }
        if (playIndex>0){
            playIndex--;
        }
        play();

    }

    private void next() {//下一首
        stop();
        if (playIndex==musics.size()){
            playIndex=0;
        }
        if (playIndex<musics.size()){
            playIndex++;
        }
        play();

    }
    public  interface  MyCallbackSeekBar{
        public  void refresh(int currentPosition,int maxPosition);
    }
    MyCallbackSeekBar myCallbackSeekBar=null;
    public void setProgressListener(MyCallbackSeekBar myCallbackSeekBar){
        this.myCallbackSeekBar=myCallbackSeekBar;
    }
    private class PlayerLisener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mp) {
            playIndex++;
            if(playIndex==musics.size()-1){
                playIndex=0;
            }
          // stop();
           isPlay=false;
            isPaurse=false;
            recycleTag=false;
            play();
            //recycleTag=false;

        }
    }
}

