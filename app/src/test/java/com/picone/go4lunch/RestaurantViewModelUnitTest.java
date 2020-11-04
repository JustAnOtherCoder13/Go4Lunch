package com.picone.go4lunch;

import android.location.Location;

import androidx.lifecycle.Observer;

import com.picone.core.data.Generator;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurantPOJO.NearBySearch;
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
    @Mock
    Observer<Restaurant> selectedRestaurantObserver;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        restaurantViewModel = new RestaurantViewModel(addRestaurantInteractor, updateUserChosenRestaurantInteractor, getCurrentUserForEmailInteractor, fetchRestaurantFromPlaceInteractor
                , getAllPersistedRestaurantsInteractor, fetchRestaurantDistanceInteractor, fetchRestaurantDetailFromPlaceInteractor, getPredictionInteractor, sendNotificationInteractor, schedulerProvider);

        userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor, schedulerProvider);

        restaurantViewModel.getAllDbRestaurants.observeForever(allDbRestaurantsObserver);
        allRestaurants.addAll(Generator.generateRestaurants());


        restaurantViewModel.getCurrentUser.observeForever(currentUserObserver);
        currentUsers.add(userToAdd);

        restaurantViewModel.getSelectedRestaurant.observeForever(selectedRestaurantObserver);


        when(userRepository.getCurrentUserForEmail(USER_EMAIL)).thenReturn(Observable.create(emitter -> emitter.onNext(currentUsers)));

        when(restaurantRepository.getAllPersistedRestaurants()).thenReturn(Observable.create(emitter -> emitter.onNext(allRestaurants)));
        when(restaurantRepository.addRestaurant(restaurantToAdd)).thenReturn(Completable.create(emitter -> {
            Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).add(restaurantToAdd);
        }));
        restaurantViewModel.setAllDbRestaurants();
        restaurantViewModel.setCurrentUser(USER_EMAIL);
        restaurantViewModel.setInterestedUsersForRestaurant("");
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
    public void addUserToRestaurantShouldAddRestaurantWithUpdatedDailySchedule() {
        restaurantViewModel.addUserToRestaurant(restaurantToAdd, restaurantViewModel.getCurrentUser.getValue());
        assertEquals(4, Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).size());
        assertTrue(restaurantViewModel.getAllDbRestaurants.getValue().contains(restaurantToAdd));
        assertEquals("Marc", restaurantViewModel.getAllDbRestaurants.getValue().get(3).getRestaurantDailySchedules().get(0).getInterestedUsers().get(0).getName());
        //assertNotNull(restaurantViewModel.getCurrentUser.getValue().getUserDailySchedules().get(0));
    }

}
