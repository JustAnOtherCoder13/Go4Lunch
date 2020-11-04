package com.picone.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantPosition;
import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.entity.user.UserDailySchedule;
import com.picone.core.domain.interactors.SendNotificationInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserInteractor;
import com.picone.go4lunch.presentation.utils.SchedulerProvider;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

import io.reactivex.schedulers.Schedulers;

import static com.picone.core.data.ConstantParameter.TODAY;

abstract class BaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    //common values
    final String USER_EMAIL = "marc@gmail.com";
    SchedulerProvider schedulerProvider = new SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline());
    Restaurant restaurantToAdd = new Restaurant("Chez Marco", "50m", "", new RestaurantPosition(), "avenue Marco", "", "10", "", "", new ArrayList<>(), new ArrayList<>());
    User userToAdd = new User("4", "Marc", "marc@gmail.com", "", new ArrayList<>(), new SettingValues());
    UserDailySchedule userDailyScheduleToAdd = new UserDailySchedule(TODAY,restaurantToAdd.getPlaceId(),restaurantToAdd.getName());

    //mock userViewModel values
    UserViewModel userViewModel;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    GetAllUsersInteractor getAllUsersInteractor;
    @InjectMocks
    AddUserInteractor addUserInteractor;
    @InjectMocks
    UpdateUserInteractor updateUserInteractor;


    //mock RestaurantViewModelValue
    RestaurantViewModel restaurantViewModel;

    @Mock
    RestaurantRepository restaurantRepository;
    @InjectMocks
    AddRestaurantInteractor addRestaurantInteractor;
    @InjectMocks
    UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    @InjectMocks
    GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    @InjectMocks
    GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    @InjectMocks
    FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor;
    @InjectMocks
    FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor;
    @InjectMocks
    FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor;
    @InjectMocks
    GetPredictionInteractor getPredictionInteractor;
    @InjectMocks
    SendNotificationInteractor sendNotificationInteractor;
}
