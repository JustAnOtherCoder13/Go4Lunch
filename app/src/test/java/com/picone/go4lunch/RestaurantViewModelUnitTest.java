package com.picone.go4lunch;

import androidx.lifecycle.Observer;

import com.picone.core.data.Generator;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RestaurantViewModelUnitTest extends BaseTest {

    private List<Restaurant> allRestaurants = new ArrayList<>();
    private List<User> currentUsers = new ArrayList<>();

    @Mock
    Observer<List<Restaurant>> allDbRestaurantsObserver;
    @Mock
    Observer<User> currentUserObserver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //initViewModels
        restaurantViewModel = new RestaurantViewModel(addRestaurantInteractor, updateUserChosenRestaurantInteractor, getCurrentUserForEmailInteractor, fetchRestaurantFromPlaceInteractor
                , getAllPersistedRestaurantsInteractor, fetchRestaurantDistanceInteractor, fetchRestaurantDetailFromPlaceInteractor, getPredictionInteractor, sendNotificationInteractor, schedulerProvider);

        userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor, schedulerProvider);


        //initObserver
        restaurantViewModel.getAllDbRestaurants.observeForever(allDbRestaurantsObserver);
        allRestaurants.addAll(Generator.generateRestaurants());


        restaurantViewModel.getCurrentUser.observeForever(currentUserObserver);
        currentUsers.add(userToAdd);

        //init mocks return
        when(userRepository.getCurrentUserForEmail(USER_EMAIL)).thenReturn(Observable.create(emitter -> emitter.onNext(currentUsers)));

        when(restaurantRepository.getAllPersistedRestaurants()).thenReturn(Observable.create(emitter -> emitter.onNext(allRestaurants)));
        when(restaurantRepository.addRestaurant(restaurantToAdd)).thenReturn(Completable.create(emitter -> {
            if (!restaurantViewModel.getAllDbRestaurants.getValue().contains(restaurantToAdd))
            restaurantViewModel.getAllDbRestaurants.getValue().add(restaurantToAdd);
        }));

        when(restaurantRepository.updateUserChosenRestaurant(userToAdd)).thenReturn(Completable.create(emitter ->
                restaurantViewModel.getCurrentUser.getValue()));

        //set viewModels values
        restaurantViewModel.setAllDbRestaurants();
        restaurantViewModel.setCurrentUser(USER_EMAIL);
    }

    @Test
    public void testNotNull() {
        assertNotNull(restaurantViewModel);
        assertNotNull(restaurantViewModel.getAllDbRestaurants.getValue());
        assertTrue(restaurantViewModel.getAllDbRestaurants.hasObservers());
        assertNotNull(restaurantViewModel.getCurrentUser.getValue());
        assertTrue(restaurantViewModel.getCurrentUser.hasObservers());
    }

    @Test
    public void getAllDbRestaurantShouldReturnGeneratedRestaurants() {
        assertEquals(3, Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).size());
        assertEquals("Chez Jiji", restaurantViewModel.getAllDbRestaurants.getValue().get(0).getName());
    }

    @Test
    public void persistRestaurantShouldAddRestaurant(){
        restaurantViewModel.persistRestaurant(restaurantToAdd);
        assertEquals(4, Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).size());
        assertTrue(restaurantViewModel.getAllDbRestaurants.getValue().contains(restaurantToAdd));
        assertEquals("Chez Marco",restaurantViewModel.getAllDbRestaurants.getValue().get(3).getName());
    }

    @Test
    public void addUserToRestaurantShouldUpdatedRestaurantDailySchedule() {
        restaurantViewModel.persistRestaurant(restaurantToAdd);
        assertTrue(restaurantViewModel.getAllDbRestaurants.getValue().get(3).getRestaurantDailySchedules().isEmpty());
        restaurantViewModel.addUserToRestaurant(restaurantToAdd,allRestaurants);
        assertEquals("Marc", restaurantViewModel.getAllDbRestaurants.getValue().get(3).getRestaurantDailySchedules().get(0).getInterestedUsers().get(0).getName());
        assertEquals(4, restaurantViewModel.getAllDbRestaurants.getValue().size());
    }

    @Test
    public void updateUserDailyScheduleShouldUpdateUser(){
        //create if not existing
        restaurantViewModel.updateUserDailySchedule(userToAdd,restaurantToAdd);
        assertNotNull(restaurantViewModel.getCurrentUser.getValue().getUserDailySchedules().get(0));
        assertEquals(restaurantToAdd.getName(),restaurantViewModel.getCurrentUser.getValue().getUserDailySchedules().get(0).getRestaurantName());
        //and replace if existing
        restaurantViewModel.updateUserDailySchedule(userToAdd,Generator.generateRestaurants().get(0));
        assertEquals(Generator.generateRestaurants().get(0).getName(),restaurantViewModel.getCurrentUser.getValue().getUserDailySchedules().get(0).getRestaurantName());
    }

}
