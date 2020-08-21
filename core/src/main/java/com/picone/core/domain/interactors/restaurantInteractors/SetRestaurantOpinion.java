package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.domain.entity.Restaurant;

public class SetRestaurantOpinion {

    private double note;
    private long numberOfVote;
    private Restaurant restaurant;

    public SetRestaurantOpinion() {
    }

    public void setRestaurantOpinion(double note, long numberOfVote, Restaurant restaurant) {
        double globalNote = restaurant.getAverageSatisfaction();
        double note1 = (globalNote + note) / numberOfVote;
    }
}
