package com.fkl.story.entity;

import java.util.List;

/**
 * Created by adner on 2016/10/28.
 */
public class Comments {
    private String id;
    private String comments;
    private Long time;
    private String sid;
    private String uid;
    private String cid;
    private List<User> users;

    public Comments() {
    }

    public Comments(String id, List<User> users, String uid, String cid, String sid, Long time, String comments) {
        this.id = id;
        this.users = users;
        this.uid = uid;
        this.cid = cid;
        this.sid = sid;
        this.time = time;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
