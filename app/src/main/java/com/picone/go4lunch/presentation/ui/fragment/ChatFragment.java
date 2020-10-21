package com.picone.go4lunch.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.core.domain.entity.ChatMessage;
import com.picone.go4lunch.databinding.FragmentChatBinding;
import com.picone.go4lunch.presentation.ui.fragment.adapters.ChatRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends BaseFragment {

    private FragmentChatBinding mBinding;
    private ChatRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater,container,false);
        showAppBars(false);
        setStatusBarTransparent(false);
        mAdapter = new ChatRecyclerViewAdapter(new ArrayList<>());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChatViewModel.getAllMessages.observe(requireActivity(),chatMessages -> {
            if (chatMessages!=null)
            mAdapter.updateMessages(chatMessages);
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewChatFragment.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewChatFragment.setAdapter(mAdapter);
        ChatMessage chatMessage = new ChatMessage("yo","what's","up hjyt hjfhjg jh lhjh lkjg gfy i oi");
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);
        mAdapter.updateMessages(chatMessages);
    }
}