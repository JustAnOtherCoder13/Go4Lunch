package com.picone.go4lunch.presentation.ui.fragments.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
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
import com.picone.go4lunch.databinding.RecyclerViewRestaurantItemsBinding;
import com.picone.go4lunch.presentation.ui.fragments.WorkmatesFragment;

import java.util.List;

public class ColleagueRecyclerViewAdapter extends RecyclerView.Adapter<ColleagueRecyclerViewAdapter.ViewHolder> {

    private List<User> mUsers;
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
            if (tag.equals(WorkmatesFragment.TAG))
                holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(" is eating ").concat("food type ").concat("(selected restaurant)"));
            else
                holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(" is joining!"));
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
}
