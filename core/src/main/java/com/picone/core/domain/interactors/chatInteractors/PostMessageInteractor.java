package com.picone.core.domain.interactors.chatInteractors;

import com.picone.core.data.repository.chat.ChatMessageRepository;
import com.picone.core.domain.entity.ChatMessage;

import javax.inject.Inject;

import io.reactivex.Completable;

public class PostMessageInteractor {

    @Inject
    ChatMessageRepository chatMessageDataSource;

    public PostMessageInteractor(ChatMessageRepository chatMessageDataSource) {
        this.chatMessageDataSource = chatMessageDataSource;
    }

    public Completable postMessage(ChatMessage chatMessage){
        return chatMessageDataSource.postMessage(chatMessage);
    }
}
