package com.picone.core.domain.interactors.chatInteractors;

import com.picone.core.data.repository.chat.ChatMessageRepository;
import com.picone.core.domain.entity.ChatMessage;

import io.reactivex.Completable;

public class PostMessageInteractor extends ChatBaseInteractor {

    public PostMessageInteractor(ChatMessageRepository chatMessageDataSource) {
        super(chatMessageDataSource);
    }

    public Completable postMessage(ChatMessage chatMessage) {
        return chatMessageDataSource.postMessage(chatMessage);
    }
}
