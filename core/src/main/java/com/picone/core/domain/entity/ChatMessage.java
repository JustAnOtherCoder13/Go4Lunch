package com.picone.core.domain.entity;

public class ChatMessage {

    private String time;
    private String userAvatar;
    private String userName;
    private String userText;
    private String uid;

    @SuppressWarnings("unused")
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

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getUserText() {
        return userText;
    }

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }
}