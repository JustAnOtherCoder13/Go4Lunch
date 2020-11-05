package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.ChatMessage;
import com.picone.core.domain.interactors.chatInteractors.GetAllMessagesInteractor;
import com.picone.core.domain.interactors.chatInteractors.PostMessageInteractor;
import com.picone.go4lunch.presentation.utils.SchedulerProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatViewModel extends BaseViewModel {

    private MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();

    private GetAllMessagesInteractor getAllMessagesInteractor;
    private PostMessageInteractor postMessageInteractor;
    private SchedulerProvider schedulerProvider;

    @ViewModelInject
    public ChatViewModel(GetAllMessagesInteractor getAllMessagesInteractor, PostMessageInteractor postMessageInteractor,SchedulerProvider schedulerProvider) {
        this.getAllMessagesInteractor = getAllMessagesInteractor;
        this.postMessageInteractor = postMessageInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public LiveData<List<ChatMessage>> getAllMessages = chatMessageMutableLiveData;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setAllMessages() {
        getAllMessagesInteractor.getAllMessages()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(chatMessages ->
                        chatMessageMutableLiveData.setValue(chatMessages),throwable -> checkException());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void postMessage(ChatMessage chatMessage) {
        postMessageInteractor.postMessage(chatMessage)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(()->{},throwable -> checkException());
    }
}
