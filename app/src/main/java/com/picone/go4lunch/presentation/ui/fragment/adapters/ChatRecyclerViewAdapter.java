package com.picone.go4lunch.presentation.ui.fragment.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.picone.core.domain.entity.ChatMessage;
import com.picone.go4lunch.databinding.RecyclerViewChatItemBinding;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private List<ChatMessage> mMessages;
    private String mCurrentUserUid;

    public ChatRecyclerViewAdapter(List<ChatMessage> mMessages) {
        this.mMessages = mMessages;
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewChatItemBinding binding = RecyclerViewChatItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.ViewHolder holder, int position) {
        final ChatMessage chatMessage = mMessages.get(position);
        if (mCurrentUserUid.equals(chatMessage.getUid())) initHolderForCurrentUser(holder, chatMessage);
        else initHolderForOtherUsers(holder, chatMessage);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerViewChatItemBinding recyclerViewChatItemBinding;

        public ViewHolder(@NonNull RecyclerViewChatItemBinding recyclerViewChatItemBinding) {
            super(recyclerViewChatItemBinding.getRoot());
            this.recyclerViewChatItemBinding = recyclerViewChatItemBinding;
        }
    }

    private void initHolderForOtherUsers(@NonNull ViewHolder holder, ChatMessage chatMessage) {
        holder.recyclerViewChatItemBinding.txtCardViewReceiver.setVisibility(View.VISIBLE);
        holder.recyclerViewChatItemBinding.txtCardViewSender.setVisibility(View.GONE);
        setAvatar(holder.recyclerViewChatItemBinding.chatAvatarImageViewReceiver, chatMessage);
        holder.recyclerViewChatItemBinding.chatMessageTxtReceiver.setText(chatMessage.getUserText());
        holder.recyclerViewChatItemBinding.chatUserNameReceiver.setText(chatMessage.getUserName());
        holder.recyclerViewChatItemBinding.chatMessageDateReceiver.setText(chatMessage.getTime());
    }

    private void initHolderForCurrentUser(@NonNull ViewHolder holder, ChatMessage chatMessage) {
        holder.recyclerViewChatItemBinding.txtCardViewReceiver.setVisibility(View.GONE);
        holder.recyclerViewChatItemBinding.txtCardViewSender.setVisibility(View.VISIBLE);
        setAvatar(holder.recyclerViewChatItemBinding.chatAvatarImageView, chatMessage);
        holder.recyclerViewChatItemBinding.chatMessageTxt.setText(chatMessage.getUserText());
        holder.recyclerViewChatItemBinding.chatUserName.setText(chatMessage.getUserName());
        holder.recyclerViewChatItemBinding.chatMessageDate.setText(chatMessage.getTime());
    }

    private void setAvatar(@NonNull ImageView chatAvatarImageView, @NonNull ChatMessage chatMessage) {
        Glide.with(chatAvatarImageView.getContext())
                .load(chatMessage.getUserAvatar())
                .circleCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        chatAvatarImageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void updateMessages(List<ChatMessage> chatMessages, String currentUserUid) {
        this.mMessages = chatMessages;
        this.mCurrentUserUid = currentUserUid;
        notifyDataSetChanged();
    }
}
