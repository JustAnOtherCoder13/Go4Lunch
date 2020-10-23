package com.picone.core.domain.entity;

public class ChatMessage {

    private String time;
    private String userAvatar;
    private String userName;
    private String userText;
    private String uid;

    public ChatMessage() {
    }

    public ChatMessage(String time, String userAvatar, String userName, String userText, String uid) {
        this.time = time;
        this.userAvatar = userAvatar;
        this.userText = userText;
        this.userName = userName;
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
