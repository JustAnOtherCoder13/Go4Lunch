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
import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.databinding.FragmentChatBinding;
import com.picone.go4lunch.presentation.ui.fragment.adapters.ChatRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;
import java.util.Objects;

import static com.picone.go4lunch.presentation.utils.ConstantParameter.TODAY;


public class ChatFragment extends BaseFragment {

    private FragmentChatBinding mBinding;
    private ChatRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);
        showAppBars(false);
        setStatusBarTransparent(false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        mBinding.postMessageFab.setOnClickListener(v -> {
            User user = mRestaurantViewModel.getCurrentUser.getValue();
            if (!mBinding.chatEditText.getText().toString().trim().isEmpty()) {
                assert user != null;
                mChatViewModel.postMessage(new ChatMessage(TODAY, user.getAvatar(), user.getName(), mBinding.chatEditText.getText().toString(), user.getUid()));
            }
            mBinding.chatEditText.getText().clear();
        });
        //TODO first click doesn't scroll down
        mBinding.chatEditText.setOnClickListener(v ->
                mBinding.recyclerViewChatFragment.post(() ->
                        mBinding.recyclerViewChatFragment.smoothScrollToPosition(mAdapter.getItemCount()))
        );
    }

    private void initRecyclerView() {
        mAdapter = new ChatRecyclerViewAdapter(new ArrayList<>());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewChatFragment.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewChatFragment.setAdapter(mAdapter);
        mChatViewModel.getAllMessages.observe(requireActivity(), chatMessages -> {
            if (chatMessages != null) {
                mAdapter.updateMessages(chatMessages, Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue()).getUid());
                mBinding.recyclerViewChatFragment.post(() ->
                        mBinding.recyclerViewChatFragment.smoothScrollToPosition(mAdapter.getItemCount()));
            }
        });
    }
}