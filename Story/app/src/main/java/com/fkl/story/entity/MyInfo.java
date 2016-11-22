package com.fkl.story.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 个人信息实体类
 */
public class MyInfo implements Serializable{
    private String id;
    private String username;
    private String userpass;
    private int  usersex;
    private String useremail;
    private String nickname;
    private String birthday;
    private String portrait;

    private static MyInfo myInfo = null;
    private List<MyInfo> list = new LinkedList<>();

    private MyInfo() {


    }

    public synchronized static MyInfo getInstace() {
        if (null == myInfo) {
            myInfo = new MyInfo();
        }
        return myInfo;
    }

    public void addUser(MyInfo myNickName) {
        list.add(myInfo);
    }

    public List<MyInfo> getMyselfInfo() {

        return list;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public int getUsersex() {
        return usersex;
    }

    public void setUsersex(int usersex) {
        this.usersex = usersex;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    private String signature;
}

