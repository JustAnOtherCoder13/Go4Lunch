package com.picone.go4lunch;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RestaurantViewModelUnitTest extends BaseTest {



    @Test
    public void testNotNull() {
        assertNotNull(restaurantViewModel);
        assertNotNull(restaurantViewModel.getAllDbRestaurants.getValue());
        assertTrue(restaurantViewModel.getAllDbRestaurants.hasObservers());
        assertNotNull(restaurantViewModel.getCurrentUser.getValue());
        assertTrue(restaurantViewModel.getCurrentUser.hasObservers());
        assertNotNull(restaurantViewModel.getAllRestaurants.getValue());
        assertTrue(restaurantViewModel.getAllRestaurants.hasObservers());
        assertEquals(3,restaurantViewModel.getAllRestaurants.getValue().size());
    }

    @Test
    public void getAllDbRestaurantShouldReturnGeneratedRestaurants() {
        assertEquals(3, Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).size());
        assertEquals(Generator.generateRestaurants().get(0).getName(), restaurantViewModel.getAllDbRestaurants.getValue().get(0).getName());
    }

    @Test
    public void persistRestaurantShouldAddRestaurant(){
        restaurantViewModel.persistRestaurant(restaurantToAdd);
        assertEquals(4, Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).size());
        assertTrue(restaurantViewModel.getAllDbRestaurants.getValue().contains(restaurantToAdd));
        assertEquals(restaurantToAdd.getName(),restaurantViewModel.getAllDbRestaurants.getValue().get(3).getName());
    }

    @Test
    public void addUserToRestaurantShouldUpdatedRestaurantDailySchedule() {
        restaurantViewModel.persistRestaurant(restaurantToAdd);
        assertTrue(Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).get(3).getRestaurantDailySchedules().isEmpty());
        restaurantViewModel.addUserToRestaurant(restaurantToAdd,allRestaurants);
        assertEquals(userToAdd.getName(), restaurantViewModel.getAllDbRestaurants.getValue().get(3).getRestaurantDailySchedules().get(0).getInterestedUsers().get(0).getName());
        assertEquals(4, restaurantViewModel.getAllDbRestaurants.getValue().size());
    }

    @Test
    public void updateUserDailyScheduleShouldUpdateUser(){
        //create if not existing
        restaurantViewModel.updateUserDailySchedule(userToAdd,restaurantToAdd);
        assertNotNull(Objects.requireNonNull(restaurantViewModel.getCurrentUser.getValue()).getUserDailySchedules().get(0));
        assertEquals(restaurantToAdd.getName(),restaurantViewModel.getCurrentUser.getValue().getUserDailySchedules().get(0).getRestaurantName());
        //and replace if existing
        restaurantViewModel.updateUserDailySchedule(userToAdd,Generator.generateRestaurants().get(0));
        assertEquals(Generator.generateRestaurants().get(0).getName(),restaurantViewModel.getCurrentUser.getValue().getUserDailySchedules().get(0).getRestaurantName());
    }

    @Test
    public void cancelReservationShouldUpdateRestaurantAndUserDailySchedule(){
        //updateValue
        restaurantViewModel.addUserToRestaurant(restaurantToAdd,allRestaurants);
        restaurantViewModel.updateUserDailySchedule(userToAdd,restaurantToAdd);
        assertNotNull(Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).get(3).getRestaurantDailySchedules().get(0));
        assertNotNull(Objects.requireNonNull(restaurantViewModel.getCurrentUser.getValue()).getUserDailySchedules().get(0));
        //cancel reservation update restaurant and user
        restaurantViewModel.cancelReservation(allRestaurants);
        assertTrue(restaurantViewModel.getAllDbRestaurants.getValue().get(3).getRestaurantDailySchedules().isEmpty());
        assertTrue(restaurantViewModel.getCurrentUser.getValue().getUserDailySchedules().isEmpty());
    }

    @Test
    public void likeRestaurantShouldUpdateFanList(){
        //update values
        restaurantViewModel.persistRestaurant(restaurantToAdd);
        assertTrue(Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).get(3).getFanList().isEmpty());

        restaurantViewModel.updateFanList(restaurantToAdd);
        //update fan list add user uid in fan list
        assertEquals(1,restaurantViewModel.getAllDbRestaurants.getValue().get(3).getFanList().size());
        assertEquals(userToAdd.getUid(), restaurantViewModel.getAllDbRestaurants.getValue().get(3).getFanList().get(0));
    }

    @Test
    public void filterByRestaurantNameShouldFilterRestaurantsAndUsers(){
        restaurantViewModel.updateUserDailySchedule(Generator.generateUsers().get(0),Generator.generateRestaurants().get(0));
        restaurantViewModel.filterExistingResults(Generator.generateRestaurants().get(0).getName(),userViewModel.getAllUsers().getValue(),locationStr,GOOGLE_KEY);
        assertFalse(Objects.requireNonNull(restaurantViewModel.getAllRestaurants.getValue()).isEmpty());
        assertEquals(Generator.generateRestaurants().get(0).getName(),restaurantViewModel.getAllRestaurants.getValue().get(0).getName());
        assertFalse(Objects.requireNonNull(restaurantViewModel.getAllFilteredUsers.getValue()).isEmpty());
        assertEquals(Generator.generateUsers().get(0).getName(),restaurantViewModel.getAllFilteredUsers.getValue().get(0).getName());
    }

}
