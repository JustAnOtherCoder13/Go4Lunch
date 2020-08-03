package com.picone.core_.domain.interactors;

import com.picone.core_.domain.entity.Restaurant;

import javax.inject.Inject;

public class SetRestaurantOpinion {

    private double note;
    private long numberOfVote;
    private Restaurant restaurant;

    @Inject
    public SetRestaurantOpinion() {

    }

    public void setRestaurantOpinion(double note, long numberOfVote, Restaurant restaurant) {

        double globalNote = restaurant.getAverageSatisfaction();
        restaurant.setAverageSatisfaction((globalNote + note) / numberOfVote);
    }
}
