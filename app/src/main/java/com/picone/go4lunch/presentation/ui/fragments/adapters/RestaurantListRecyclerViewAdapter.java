package com.picone.go4lunch.presentation.ui.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.core.domain.entity.Restaurant;
import com.picone.go4lunch.databinding.RecyclerViewRestaurantItemsBinding;

import java.util.List;

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
        holder.restaurantBinding.openingTimeTextView.setText(String.valueOf(restaurant.getOpeningHours()).concat(" h"));
        holder.restaurantBinding.foodStyleAndAddressTextView.setText(restaurant.getFoodType().concat(" - ")
                .concat(restaurant.getAddress()));
        holder.restaurantBinding.distanceTextView.setText(String.valueOf(restaurant.getDistance()).concat(" M"));

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
}
