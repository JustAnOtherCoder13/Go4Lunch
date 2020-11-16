package com.picone.core.domain.interactors.chatInteractors;

import com.picone.core.data.repository.chat.ChatMessageRepository;
import com.picone.core.domain.entity.ChatMessage;

import java.util.List;

import io.reactivex.Observable;

public class GetAllMessagesInteractor extends ChatBaseInteractor{


    public GetAllMessagesInteractor(ChatMessageRepository chatMessageDataSource) {
        super(chatMessageDataSource);
    }

    public Observable<List<ChatMessage>> getAllMessages() {
        return chatMessageDataSource.getAllMessages();
    }
}
