package com.picone.go4lunch.presentation.utils;

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
import com.picone.go4lunch.presentation.ui.fragment.RestaurantListFragment;
import com.picone.go4lunch.presentation.ui.fragment.WorkmatesFragment;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<User> mUsers;
    private List<Restaurant> mRestaurants;
    private String tag = null;

    public RecyclerViewAdapter(List<Restaurant> items) {
        this.mRestaurants = items;
    }

    public RecyclerViewAdapter(List<User> items, String tag) {
        this.tag = tag;
        this.mUsers = items;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (tag == null) {
            RecyclerViewRestaurantItemsBinding binding = RecyclerViewRestaurantItemsBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        } else {
            RecyclerViewColleagueItemsBinding binding = RecyclerViewColleagueItemsBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        if (tag == null) {
            final Restaurant restaurant = mRestaurants.get(position);
            holder.restaurantBinding.restaurantNameTextView.setText(restaurant.getName());
            holder.restaurantBinding.openingTimeTextView.setText(String.valueOf(restaurant.getOpeningHours()).concat(" h"));
            holder.restaurantBinding.foodStyleAndAddressTextView.setText(restaurant.getFoodType().concat(" - ")
                    .concat(restaurant.getAddress()));
            holder.restaurantBinding.distanceTextView.setText(String.valueOf(restaurant.getDistance()).concat(" M"));

        } else {
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
    }

    @Override
    public int getItemCount() {
        if (tag == null) {
            return mRestaurants.size();
        } else {
            return mUsers.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerViewColleagueItemsBinding colleagueBinding;
        private RecyclerViewRestaurantItemsBinding restaurantBinding;


        ViewHolder(RecyclerViewColleagueItemsBinding colleagueBinding) {
            super(colleagueBinding.getRoot());
            this.colleagueBinding = colleagueBinding;
        }

        ViewHolder(RecyclerViewRestaurantItemsBinding restaurantBinding) {
            super(restaurantBinding.getRoot());
            this.restaurantBinding = restaurantBinding;
        }
    }

    public void updateUsers(List<User> users) {
        this.mUsers = users;
        notifyDataSetChanged();
    }

    public void updateRestaurants(List<Restaurant> restaurants) {
        this.mRestaurants = restaurants;
        notifyDataSetChanged();
    }
}
