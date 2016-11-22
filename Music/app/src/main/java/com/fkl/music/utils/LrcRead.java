package com.fkl.music.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adner on 2016/10/18.
 */
public class LrcRead {
    private static List<String> lyricData = new ArrayList<>();
    private static List<Integer> lyricTime = new ArrayList<>();

    //======读取歌词=========
    public static List<String> readLrc(String path) {
        lyricData.clear();
        lyricTime.clear();
        File file = new File(path.replace(".mp3",".lrc"));
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            while ((s = br.readLine()) != null) {

                if ((s.indexOf("[ti:") == -1) && (s.indexOf("[ar:") == -1) && (s.indexOf("[al:") == -1) && (s.indexOf("[by:") == -1) && (s.indexOf("[offset:") == -1)) {
                    String ss = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
                    String lrc = s.substring(s.indexOf("]") + 1, s.length());
                    lyricTime.add(timeHandler(ss));
                    lyricData.add(lrc);
                    s = "";
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lyricData;
    }

    //=========获取时间=======
    public static List<Integer> getLyricTime(){
        return lyricTime;
    }

    //========分离出时间=========
    public static int timeHandler(String string) {
        string = string.replace(".", ":");
        String timeData[] = string.split(":");
        int minute = Integer.parseInt(timeData[0]);
        int second = Integer.parseInt(timeData[1]);
        int millisecond = Integer.parseInt(timeData[2]);

        //计算上一行与下一行的事件，转换为毫秒
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        return currentTime;
    }
}
