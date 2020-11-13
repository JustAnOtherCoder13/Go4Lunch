package com.picone.core.data.repository.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.ChatMessage;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class ChatMessageDaoImpl implements ChatMessageDao {

    @Inject
    protected FirebaseDatabase database;
    private DatabaseReference chatDatabaseReference;

    public ChatMessageDaoImpl(FirebaseDatabase database) {
        this.database = database;
        this.chatDatabaseReference = database.getReference().child("chat");
    }

    @Override
    public Observable<List<ChatMessage>> getAllMessages() {
        return RxFirebaseDatabase.observeValueEvent(chatDatabaseReference.limitToLast(50),
                DataSnapshotMapper.listOf(ChatMessage.class)).toObservable();
    }

    @Override
    public Completable postMessage(ChatMessage chatMessage) {
        return RxFirebaseDatabase.setValue(chatDatabaseReference.push(), chatMessage);
    }
}
