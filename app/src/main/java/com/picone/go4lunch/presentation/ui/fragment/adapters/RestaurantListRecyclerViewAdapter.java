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
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.RecyclerViewRestaurantItemsBinding;

import java.util.List;

import static com.picone.core.utils.ConstantParameter.CLOSED;
import static com.picone.core.utils.FindInListUtil.getRestaurantDailyScheduleOnToday;
import static com.picone.go4lunch.presentation.helpers.ManageStarHelper.manageStar;

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
        setRestaurantPhoto(holder, restaurant);
        setInterestedUsers(holder, restaurant);
        setNumberOfStars(holder, restaurant);
        setOpeningHour(holder, restaurant);
        holder.restaurantBinding.restaurantNameTextView.setText(restaurant.getName());
        holder.restaurantBinding.addressTextView.setText(restaurant.getAddress());
        holder.restaurantBinding.distanceTextView.setText(restaurant.getDistance());
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerViewRestaurantItemsBinding restaurantBinding;

        ViewHolder(@NonNull RecyclerViewRestaurantItemsBinding restaurantBinding) {
            super(restaurantBinding.getRoot());
            this.restaurantBinding = restaurantBinding;
        }
    }

    public void updateRestaurants(List<Restaurant> restaurants) {
        this.mRestaurants = restaurants;
        notifyDataSetChanged();
    }

    private void setRestaurantPhoto(@NonNull ViewHolder holder, @NonNull Restaurant restaurant) {
        Glide.with(holder.restaurantBinding.restaurantPhotoImageView.getContext())
                .load(restaurant.getRestaurantPhoto())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.restaurantBinding.restaurantPhotoImageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void setNumberOfStars(@NonNull ViewHolder holder, @NonNull Restaurant restaurant) {
        int numberOfLike = 0;
        if (restaurant.getFanList() != null) numberOfLike = restaurant.getFanList().size();
        manageStar(holder.restaurantBinding.opinionStarDetailImageView, numberOfLike);
    }

    private void setInterestedUsers(@NonNull ViewHolder holder, @NonNull Restaurant restaurant) {
        if (getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null &&
                getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers().size() > 0){
            holder.restaurantBinding.interestedColleague.setVisibility(View.VISIBLE);
            holder.restaurantBinding.interestedColleagueNumber.setVisibility(View.VISIBLE);
            holder.restaurantBinding.interestedColleagueNumber.setText(("(").concat(String.valueOf(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers().size())).concat(")"));
        }else {
            holder.restaurantBinding.interestedColleague.setVisibility(View.GONE);
            holder.restaurantBinding.interestedColleagueNumber.setVisibility(View.GONE);
        }
    }

    private void setOpeningHour(@NonNull ViewHolder holder, @NonNull Restaurant restaurant) {
        String formatOpeningHour = holder.itemView.getContext().getString(R.string.open_until).concat(restaurant.getOpeningHours());
        if (restaurant.getOpeningHours().equalsIgnoreCase(CLOSED)){
            holder.restaurantBinding.openingTimeTextView.setText(R.string.closed);
            holder.restaurantBinding.openingTimeTextView.setTextColor(Color.RED);
        }
        else{
            holder.restaurantBinding.openingTimeTextView.setTextColor(Color.GRAY);
            holder.restaurantBinding.openingTimeTextView.setText(formatOpeningHour);
        }
    }
}
