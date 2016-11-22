package com.fkl.story.entity;

import java.io.Serializable;

/**
 * Created by adner on 2016/10/26.
 */
public class User implements Serializable{
   private String id;
    private String    username;
    private String    usersex;
    private String     useremail;
    private String      nickname;
    private String    birthday;
    private String     portrait;
    private String    signature;

    public User() {
    }

    public User(String id, String signature, String portrait, String birthday, String nickname, String useremail, String usersex, String username) {
        this.id = id;
        this.signature = signature;
        this.portrait = portrait;
        this.birthday = birthday;
        this.nickname = nickname;
        this.useremail = useremail;
        this.usersex = usersex;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsersex() {
        return usersex;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
