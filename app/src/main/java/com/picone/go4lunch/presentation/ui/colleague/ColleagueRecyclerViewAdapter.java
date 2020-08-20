package com.picone.go4lunch.presentation.ui.colleague;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.RecyclerViewColleagueItemsBinding;

import java.util.List;

public class ColleagueRecyclerViewAdapter extends RecyclerView.Adapter<ColleagueRecyclerViewAdapter.ViewHolder> {

    private List<User> mUsers;

    public ColleagueRecyclerViewAdapter(List<User> items) {
        mUsers = items;
    }

    @NonNull
    @Override
    public ColleagueRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewColleagueItemsBinding binding = RecyclerViewColleagueItemsBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColleagueRecyclerViewAdapter.ViewHolder holder, int position) {

        final User user = mUsers.get(position);
        holder.binding.userSelectedRestaurant.setText(user.getName().concat(" is eating ").concat("food type ").concat("(selected restaurant)"));
        Glide.with(holder.binding.avatarImageView.getContext())
                .load(user.getAvatar())
                .circleCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.binding.avatarImageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerViewColleagueItemsBinding binding;

        public ViewHolder(RecyclerViewColleagueItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateUsers(List<User> users){
        this.mUsers = users;
        notifyDataSetChanged();
    }
}
