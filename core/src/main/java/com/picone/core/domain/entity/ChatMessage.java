package com.picone.core.domain.entity;

public class ChatMessage {

    private String time;
    private String userAvatar;
    private String userText;

    public ChatMessage() {
    }

    public ChatMessage(String time, String userAvatar, String userText) {
        this.time = time;
        this.userAvatar = userAvatar;
        this.userText = userText;
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
}
