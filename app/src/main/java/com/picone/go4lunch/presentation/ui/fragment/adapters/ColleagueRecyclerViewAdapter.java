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
import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.RecyclerViewColleagueItemsBinding;
import com.picone.go4lunch.presentation.ui.fragment.WorkmatesFragment;

import java.util.List;

import static com.picone.core.utils.FindInListUtil.getUserDailyScheduleOnToday;

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
        if (tag.equals(WorkmatesFragment.TAG)) initHolderForWorkmatesFragment(holder, user);
        else initHolderForRestaurantDetail(holder, user);
        initUserAvatar(holder, user);
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

    private void initHolderForRestaurantDetail(@NonNull ViewHolder holder, User user) {
        holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(holder.itemView.getContext().getString(R.string.is_joining)));
        holder.colleagueBinding.separator.setVisibility(View.GONE);
    }

    private void initHolderForWorkmatesFragment(@NonNull ViewHolder holder, User user) {
        if (user.getUserDailySchedules() != null && getUserDailyScheduleOnToday(user.getUserDailySchedules()) != null) {
            holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(holder.itemView.getContext().getString(R.string.is_eating)).concat(getUserDailyScheduleOnToday(user.getUserDailySchedules()).getRestaurantName()));
            holder.colleagueBinding.userSelectedRestaurant.setTextColor(Color.BLACK);
        } else {
            holder.colleagueBinding.userSelectedRestaurant.setText(user.getName().concat(holder.itemView.getContext().getString(R.string.has_not_decide)));
            holder.colleagueBinding.userSelectedRestaurant.setTextColor(Color.LTGRAY);
            holder.colleagueBinding.userSelectedRestaurant.setTypeface(holder.colleagueBinding.userSelectedRestaurant.getTypeface(), Typeface.ITALIC);
        }
    }

    private void initUserAvatar(@NonNull ViewHolder holder, User user) {
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