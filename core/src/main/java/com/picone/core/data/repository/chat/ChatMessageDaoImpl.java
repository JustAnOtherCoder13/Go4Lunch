package com.picone.core.data.repository.chat;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.ChatMessage;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.picone.core.utils.ConstantParameter.CHAT_MESSAGE_LIMIT;
import static com.picone.core.utils.ConstantParameter.CHAT_REF;

public class ChatMessageDaoImpl implements ChatMessageDao {

    @Inject
    protected FirebaseDatabase database;
    private DatabaseReference chatDatabaseReference;

    public ChatMessageDaoImpl(@NonNull FirebaseDatabase database) {
        this.database = database;
        this.chatDatabaseReference = database.getReference().child(CHAT_REF);
    }

    @Override
    public Observable<List<ChatMessage>> getAllMessages() {
        return RxFirebaseDatabase.observeValueEvent(chatDatabaseReference.limitToLast(CHAT_MESSAGE_LIMIT),
                DataSnapshotMapper.listOf(ChatMessage.class)).toObservable();
    }

    @Override
    public Completable postMessage(ChatMessage chatMessage) {
        return RxFirebaseDatabase.setValue(chatDatabaseReference.push(), chatMessage);
    }
}
