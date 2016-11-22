package com.fkl.story.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 故事实体类
 */
public class Story implements Serializable{
    private String  id;                       //故事id
    private Long      story_time;          //发表时间
    private String     story_info;         //故事内容
    private List<String>  pics;             //图片地址
    private String     uid;
    private String    lng;                 //经度
    private String     lat;                //纬度
    private String    city;                //城市
    private int     readcount;            //阅读量
    private int     comment;              //评论数量
    private List<Story> storys=new ArrayList<>();//把故事加入List里
    private List<User> list;                     //用户信息
    public Story() {
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public Story(String id, Long story_time, String story_info, List<String>  pics, String uid, String lng, String lat, String city, int readcount, int comment, List<User> list) {

        this.id = id;
        this.story_time = story_time;
        this.story_info = story_info;
        this.pics = pics;
        this.uid = uid;
        this.lng = lng;
        this.lat = lat;
        this.city = city;
        this.readcount = readcount;
        this.comment = comment;
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStory_time() {
        return story_time;
    }

    public void setStory_time(Long story_time) {
        this.story_time = story_time;
    }

    public String getStory_info() {
        return story_info;
    }

    public void setStory_info(String story_info) {
        this.story_info = story_info;
    }

    public List<String>  getPics() {
        return pics;
    }

    public void setPics(List<String>  pics) {
        this.pics = pics;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getReadcount() {
        return readcount;
    }

    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
    public void addStory(List<Story> list){
        storys.addAll(list);

    }
    public List<Story> getStorys(){
        return storys;
    }
}
