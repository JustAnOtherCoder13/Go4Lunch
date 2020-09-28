package com.picone.go4lunch.presentation.ui.fragment.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.RecyclerViewColleagueItemsBinding;
import com.picone.go4lunch.presentation.ui.fragment.WorkmatesFragment;

import java.util.List;

public class ColleagueRecyclerViewAdapter extends RecyclerView.Adapter<ColleagueRecyclerViewAdapter.ViewHolder> {

    private List<User> mUsers;
    private Restaurant userChosenRestaurant;
    private String tag;

    public ColleagueRecyclerViewAdapter(List<User> items, String tag) {
        this.tag = tag;
        this.mUsers = items;
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
        if (tag.equals(WorkmatesFragment.TAG)) {
            if (userChosenRestaurant != null) {
                holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(" is eating ").concat(userChosenRestaurant.getFoodType()).concat(" ").concat(userChosenRestaurant.getName()));
                holder.colleagueBinding.userSelectedRestaurant.setTextColor(Color.BLACK);
            } else {
                holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(" hasn't decided yet"));
                holder.colleagueBinding.userSelectedRestaurant.setTextColor(Color.LTGRAY);
                holder.colleagueBinding.userSelectedRestaurant.setTypeface(holder.colleagueBinding.userSelectedRestaurant.getTypeface(), Typeface.ITALIC);
            }
        } else {
            holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(" is joining!"));
            holder.colleagueBinding.separator.setVisibility(View.GONE);
        }
        Glide.with(holder.colleagueBinding.avatarImageView.getContext())
                .load(user.getAvatar())
                .circleCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.colleagueBinding.avatarImageView.setImageDrawable(resource);
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

        private RecyclerViewColleagueItemsBinding colleagueBinding;

        ViewHolder(RecyclerViewColleagueItemsBinding colleagueBinding) {
            super(colleagueBinding.getRoot());
            this.colleagueBinding = colleagueBinding;
        }
    }

    public void updateUsers(List<User> users) {
        this.mUsers = users;
        notifyDataSetChanged();
    }

    public void getUserChosenRestaurant(Restaurant userChosenRestaurant) {
        this.userChosenRestaurant = userChosenRestaurant;
    }
}