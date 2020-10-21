package com.picone.go4lunch.presentation.ui.fragment.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
        Log.i("TAG", "onBindViewHolder: "+mMessages.get(position).getUserName());
        final ChatMessage chatMessage = mMessages.get(position);
        holder.recyclerViewChatItemBinding.chatMessageTxt.setText(chatMessage.getUserText());
        holder.recyclerViewChatItemBinding.chatUserName.setText(chatMessage.getUserName());
        holder.recyclerViewChatItemBinding.chatMessageDate.setText(chatMessage.getTime());
        Glide.with(holder.recyclerViewChatItemBinding.chatAvatarImageView.getContext())
                .load(chatMessage.getUserAvatar())
                .circleCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.recyclerViewChatItemBinding.chatAvatarImageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerViewChatItemBinding recyclerViewChatItemBinding;

        public ViewHolder(RecyclerViewChatItemBinding recyclerViewChatItemBinding) {
            super(recyclerViewChatItemBinding.getRoot());
            this.recyclerViewChatItemBinding = recyclerViewChatItemBinding;
        }
    }

    public void updateMessages(List<ChatMessage> chatMessages){
        this.mMessages=chatMessages;
    }

}
