package com.picone.go4lunch.presentation.ui.fragment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.core.domain.entity.ChatMessage;
import com.picone.go4lunch.databinding.RecyclerViewChatItemBinding;
import com.picone.go4lunch.databinding.RecyclerViewColleagueItemsBinding;

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

        final ChatMessage chatMessage = mMessages.get(position);
        holder.recyclerViewChatItemBinding.messageTxt.setText(chatMessage.getUserText());
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
