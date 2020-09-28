package com.picone.go4lunch.presentation.ui.fragment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.core.domain.entity.Restaurant;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.RecyclerViewRestaurantItemsBinding;
import com.picone.go4lunch.databinding.StarLayoutBinding;

import java.util.List;

import static com.picone.go4lunch.presentation.ui.utils.ManageStarUtil.manageStar;

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
        holder.restaurantBinding.distanceTextView.setText(String.valueOf(restaurant.getDistance()).concat(" m"));
        if (restaurant.getNumberOfInterestedUsers()>0)
            holder.restaurantBinding.interestedColleagueNumber.setText("(".concat(String.valueOf(restaurant.getNumberOfInterestedUsers())).concat(")"));
        else {
            holder.restaurantBinding.interestedColleague.setVisibility(View.GONE);
            holder.restaurantBinding.interestedColleagueNumber.setVisibility(View.GONE);}
        manageStar(holder.restaurantBinding.opinionStarDetailImageView, (int)restaurant.getAverageSatisfaction());
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