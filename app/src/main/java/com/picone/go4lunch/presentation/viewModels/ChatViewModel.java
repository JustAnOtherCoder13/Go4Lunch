package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.ChatMessage;
import com.picone.core.domain.interactors.chatInteractors.GetAllMessagesInteractor;
import com.picone.core.domain.interactors.chatInteractors.PostMessageInteractor;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<List<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();

    private GetAllMessagesInteractor getAllMessagesInteractor;
    private PostMessageInteractor postMessageInteractor;

    @ViewModelInject
    public ChatViewModel(GetAllMessagesInteractor getAllMessagesInteractor, PostMessageInteractor postMessageInteractor) {
        this.getAllMessagesInteractor = getAllMessagesInteractor;
        this.postMessageInteractor = postMessageInteractor;
    }

    public LiveData<List<ChatMessage>> getAllMessages = chatMessageMutableLiveData;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setAllMessages (){
        getAllMessagesInteractor.getAllMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatMessages -> {
                    chatMessageMutableLiveData.setValue(chatMessages);
                });
    }

    public void postMessage(ChatMessage chatMessage){
        postMessageInteractor.postMessage(chatMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
