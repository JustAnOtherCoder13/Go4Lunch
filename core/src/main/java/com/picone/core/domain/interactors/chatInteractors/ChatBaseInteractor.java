package com.picone.core.domain.interactors.chatInteractors;

import com.picone.core.data.repository.chat.ChatMessageRepository;

import javax.inject.Inject;

public abstract class ChatBaseInteractor {

    @Inject
    protected ChatMessageRepository chatMessageDataSource;

    public ChatBaseInteractor(ChatMessageRepository chatMessageDataSource){
        this.chatMessageDataSource = chatMessageDataSource;
    }
}
