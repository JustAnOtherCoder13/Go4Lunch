package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.RestaurantDaoImpl;
import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.interactors.restaurantInteractors.AddDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.UpdateInterestedUsersInteractor;
import com.picone.core.domain.interactors.userInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.userInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.userInteractors.GetUserInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

import static com.picone.core.data.mocks.Generator.RESTAURANTS;

@InstallIn(ActivityComponent.class)
@Module
public final class coreModule {

    @Singleton
    @Provides
    static FirebaseDatabase provideFireBaseDataBase() {
        return FirebaseDatabase.getInstance();
    }

    @Singleton
    @Provides
    static UserRepository provideUserDataSource() {
        return new UserRepository(provideFireBaseDataBase(), provideUserDaoImpl());
    }

    @Singleton
    @Provides
    static RestaurantRepository provideRestaurantDataSource() {
        return new RestaurantRepository(provideFireBaseDataBase(), provideRestaurantDaoImpl());
    }

    //DAO
    @Provides
    static UserDaoImpl provideUserDaoImpl() {
        return new UserDaoImpl(provideFireBaseDataBase());
    }

    @Provides
    static RestaurantDaoImpl provideRestaurantDaoImpl() {
        return new RestaurantDaoImpl(provideFireBaseDataBase());
    }

    //generator
    @Provides
    static List<Restaurant> provideGenerateRestaurant() {
        return new ArrayList<>(RESTAURANTS);
    }

    //user interactors
    @Provides
    static GetAllUsersInteractor provideGetAllUsers() {
        return new GetAllUsersInteractor(provideUserDataSource());
    }

    @Provides
    static GetUserInteractor provideGetUser() {
        return new GetUserInteractor(provideUserDataSource());
    }

    @Provides
    static AddUserInteractor provideAddUser() {
        return new AddUserInteractor(provideUserDataSource());
    }

    //restaurant interactors
    @Provides
    static GetAllRestaurantsInteractor provideGetAllRestaurants() {
        return new GetAllRestaurantsInteractor(provideRestaurantDataSource(), provideGenerateRestaurant());
    }

    @Provides
    static GetRestaurantInteractor provideGetRestaurant() {
        return new GetRestaurantInteractor(provideRestaurantDataSource(), provideGenerateRestaurant());
    }

    @Provides
    static AddRestaurantInteractor provideAddRestaurant(){
        return  new AddRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetDailyScheduleInteractor provideGetDailySchedule(){
        return new GetDailyScheduleInteractor(provideRestaurantDataSource());
    }


    @Provides
    static AddDailyScheduleInteractor provideAddDailySchedule(){
    return new AddDailyScheduleInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetInterestedUsersForRestaurantInteractor provideGetInterestedUsersForRestaurant(){
        return new GetInterestedUsersForRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static UpdateInterestedUsersInteractor provideUpdateInterestedUsers(){
        return new UpdateInterestedUsersInteractor(provideRestaurantDataSource());
    }
}
