package com.picone.core.domain.interactors.chatInteractors;

import com.picone.core.data.repository.chat.ChatMessageRepository;
import com.picone.core.domain.entity.ChatMessage;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAllMessagesInteractor {

    @Inject
    protected ChatMessageRepository chatMessageDataSource;

    public GetAllMessagesInteractor(ChatMessageRepository chatMessageDataSource) {
        this.chatMessageDataSource = chatMessageDataSource;
    }

    public Observable<List<ChatMessage>> getAllMessages() {
        return chatMessageDataSource.getAllMessages();
    }
}
