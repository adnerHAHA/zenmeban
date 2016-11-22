package com.fkl.music.bean;


import java.io.Serializable;

public class MusicInfo implements Serializable{
    private int sid;
    private  String title;
    private  String singer;
    private String  album;
    private long size;
    private long time;
    private String url;
    private String name;
    private String sbr;

    public MusicInfo() {

    }

    public MusicInfo(String title, String singer, String name, String url, long time, String album, long size, String sbr) {
        this.title = title;
        this.singer = singer;
        this.name = name;
        this.url = url;
        this.time = time;
        this.album = album;
        this.size = size;
        this.sbr = sbr;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSbr() {
        return sbr;
    }

    public void setSbr(String sbr) {
        this.sbr = sbr;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "title='" + title + '\'' +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", size=" + size +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", sbr='" + sbr + '\'' +
                '}';
    }
}
