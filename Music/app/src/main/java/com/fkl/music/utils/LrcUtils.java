package com.fkl.music.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adner on 2016/10/15.
 */
public class LrcUtils {
    private List<LrcContent> lrcs=new ArrayList<>();
    private LrcContent lrcContent;

    public   List<LrcContent> readLRC(String path){
        lrcs.clear();
        FileInputStream fis=null;
        InputStreamReader isr=null;
        BufferedReader br=null;

        StringBuffer stringBuffer=new StringBuffer();
        File f=new File(path.replace(".mp3",".lrc"));

        try {
            fis = new FileInputStream(f);
             isr=new InputStreamReader(fis,"UTF-8");
             br = new BufferedReader(isr);
            String s="";
            while (null!=(s=br.readLine())){
                lrcContent=new LrcContent();
                s.replace("[","");
                s.replace("]","@");
                String[] lrcData=s.split("@");
                if (lrcData.length>0){
                    lrcContent.setLrc(lrcData[1]);
                    int lrcTime=timeStr(lrcData[0]);
                    lrcContent.setLrcTime(lrcTime);
                    lrcs.add(lrcContent);
                    lrcContent=new LrcContent();

                }


            }
          return lrcs;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=br){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
                if (null!=isr){
                    try {
                        isr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (null!=fis){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    }
                }

            }


        }
        return lrcs;
    }

    public List<LrcContent> getLrcContent(){
        return lrcs;
    }
    public int timeStr(String timeStr){

        timeStr = timeStr.replace(":", ".");
        timeStr = timeStr.replace(".", "@");

        String timeData[] = timeStr.split("@");

        // 分离出分、秒并转换为整型
        int minute = Integer.parseInt(timeData[0]);
        int second = Integer.parseInt(timeData[1]);
        int millisecond = Integer.parseInt(timeData[2]);

        // 计算上一行与下一行的时间转换为毫秒数
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;

        return currentTime;

    }
    public class  LrcContent{
        public String lrc;
        public int lrcTime;

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public int getLrcTime() {
            return lrcTime;
        }

        public void setLrcTime(int lrcTime) {
            this.lrcTime = lrcTime;
        }
    }
}
