package com.picone.go4lunch.presentation.ui.fragment.adapters;

import android.graphics.Color;
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
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.go4lunch.databinding.RecyclerViewRestaurantItemsBinding;

import java.util.List;

import static com.picone.go4lunch.presentation.utils.ManageStarUtil.manageStar;

public class RestaurantListRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantListRecyclerViewAdapter.ViewHolder> {

    private List<Restaurant> mRestaurants;

    public RestaurantListRecyclerViewAdapter(List<Restaurant> items) {
        this.mRestaurants = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewRestaurantItemsBinding binding = RecyclerViewRestaurantItemsBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RestaurantListRecyclerViewAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Restaurant restaurant = mRestaurants.get(position);
        holder.restaurantBinding.restaurantNameTextView.setText(restaurant.getName());
        setOpeningHour(holder, restaurant);
        holder.restaurantBinding.openingTimeTextView.setText(restaurant.getOpeningHours());
        holder.restaurantBinding.foodStyleAndAddressTextView.setText(restaurant.getAddress());
        holder.restaurantBinding.distanceTextView.setText(restaurant.getDistance());
        setInterestedUsers(holder, restaurant);
        setNumberOfStars(holder, restaurant);
        setRestaurantPhoto(holder, restaurant);
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerViewRestaurantItemsBinding restaurantBinding;

        ViewHolder(RecyclerViewRestaurantItemsBinding restaurantBinding) {
            super(restaurantBinding.getRoot());
            this.restaurantBinding = restaurantBinding;
        }
    }

    public void updateRestaurants(List<Restaurant> restaurants) {
        this.mRestaurants = restaurants;
        notifyDataSetChanged();
    }

    private void setRestaurantPhoto(@NonNull ViewHolder holder, Restaurant restaurant) {
        Glide.with(holder.restaurantBinding.restaurantPhotoImageView.getContext())
                .load(restaurant.getRestaurantPhoto())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.restaurantBinding.restaurantPhotoImageView.setImageDrawable(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }

    private void setNumberOfStars(@NonNull ViewHolder holder, Restaurant restaurant) {
        int numberOfLike = 0;
        if (restaurant.getFanList() != null) numberOfLike = restaurant.getFanList().size();
        manageStar(holder.restaurantBinding.opinionStarDetailImageView, numberOfLike);
    }

    private void setInterestedUsers(@NonNull ViewHolder holder, Restaurant restaurant) {
        if (restaurant.getNumberOfInterestedUsers() > 0)
            holder.restaurantBinding.interestedColleagueNumber.setText(("(").concat(String.valueOf(restaurant.getNumberOfInterestedUsers())).concat(")"));
        else {
            holder.restaurantBinding.interestedColleague.setVisibility(View.GONE);
            holder.restaurantBinding.interestedColleagueNumber.setVisibility(View.GONE);
        }
    }

    private void setOpeningHour(@NonNull ViewHolder holder, Restaurant restaurant) {
        if (restaurant.getOpeningHours().equals("Closed"))
            holder.restaurantBinding.openingTimeTextView.setTextColor(Color.RED);
        else
            holder.restaurantBinding.openingTimeTextView.setTextColor(Color.GRAY);
    }
}