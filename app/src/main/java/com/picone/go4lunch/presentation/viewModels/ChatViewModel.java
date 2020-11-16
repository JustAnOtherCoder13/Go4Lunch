package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.ChatMessage;
import com.picone.core.domain.interactors.chatInteractors.GetAllMessagesInteractor;
import com.picone.core.domain.interactors.chatInteractors.PostMessageInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends BaseViewModel {

    private MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>(new ArrayList<>());

    @ViewModelInject
    public ChatViewModel(GetAllMessagesInteractor getAllMessagesInteractor, PostMessageInteractor postMessageInteractor, SchedulerProvider schedulerProvider) {
        this.getAllMessagesInteractor = getAllMessagesInteractor;
        this.postMessageInteractor = postMessageInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public LiveData<List<ChatMessage>> getAllMessages = chatMessageMutableLiveData;

    public void setAllMessages() {
        compositeDisposable.add(
        getAllMessagesInteractor.getAllMessages()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(chatMessages ->
                        chatMessageMutableLiveData.setValue(chatMessages), throwable -> checkException()));
    }

    public void postMessage(ChatMessage chatMessage) {
        if (chatMessage != null && chatMessage.getUserText() != null && !chatMessage.getUserText().trim().isEmpty())
            compositeDisposable.add(
            postMessageInteractor.postMessage(chatMessage)
                    .subscribeOn(schedulerProvider.getIo())
                    .observeOn(schedulerProvider.getUi())
                    .subscribe(() -> {
                    }, throwable -> checkException()));
    }
}
