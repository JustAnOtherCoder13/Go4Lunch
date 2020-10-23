package com.picone.core.data.repository.chat;

import com.picone.core.domain.entity.ChatMessage;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class ChatMessageRepository {

    @Inject
    protected ChatMessageDaoImpl chatMessageDao;

    public ChatMessageRepository(ChatMessageDaoImpl chatMessageDao) {
        this.chatMessageDao = chatMessageDao;
    }

    public Observable<List<ChatMessage>> getAllMessages() {
        return chatMessageDao.getAllMessages();
    }

    public Completable postMessage(ChatMessage chatMessage) {
        return chatMessageDao.postMessage(chatMessage);
    }
    }
