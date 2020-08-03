package com.picone.core.domain.interactors;

import com.picone.core.domain.entity.Restaurant;

public class SetRestaurantOpinion {

    private double note;
    private long numberOfVote;
    private Restaurant restaurant;

    public SetRestaurantOpinion(double note, long numberOfVote, Restaurant restaurant) {
        this.note = note;
        this.numberOfVote = numberOfVote;
        this.restaurant = restaurant;
    }

    public void setRestaurantOpinion() {

        double globalNote = restaurant.getAverageSatisfaction();
        restaurant.setAverageSatisfaction((globalNote + note) / numberOfVote);
    }
}
