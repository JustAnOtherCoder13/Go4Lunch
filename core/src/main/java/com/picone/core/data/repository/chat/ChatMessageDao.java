package com.picone.core.data.repository.chat;

import com.picone.core.domain.entity.ChatMessage;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ChatMessageDao {

    Observable<List<ChatMessage>> getAllMessages();

    Completable postMessage(ChatMessage chatMessage);
}
