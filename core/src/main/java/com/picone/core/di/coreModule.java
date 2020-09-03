package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.RestaurantDaoImpl;
import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.AddDailyScheduleToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.DeleteDailyScheduleFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.GetDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.AddCurrentUserToGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.DeleteUserFromGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.GetAllGlobalUsersInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.GetGlobalCurrentUserInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.AddCurrentUserToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.DeleteCurrentUserFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.GetAllInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.userInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.userInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.userInteractors.GetCurrentUserForEmailInteractor;

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

    //--------------------------DATA---------------------------------
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

    //--------------------------DAO---------------------------------
    @Provides
    static UserDaoImpl provideUserDaoImpl() {
        return new UserDaoImpl(provideFireBaseDataBase());
    }

    @Provides
    static RestaurantDaoImpl provideRestaurantDaoImpl() { return new RestaurantDaoImpl(provideFireBaseDataBase()); }

    //--------------------------GENERATOR---------------------------------
    @Provides
    static List<Restaurant> provideGenerateRestaurant() {
        return new ArrayList<>(RESTAURANTS);
    }

    //--------------------------USER---------------------------------
    @Provides
    static GetAllUsersInteractor provideGetAllUsers() {
        return new GetAllUsersInteractor(provideUserDataSource());
    }

    @Provides
    static GetCurrentUserForEmailInteractor provideGetCurrentUserForEmail(){
        return new GetCurrentUserForEmailInteractor(provideUserDataSource());
    }

    @Provides
    static AddUserInteractor provideAddUser() {
        return new AddUserInteractor(provideUserDataSource());
    }

    //--------------------------RESTAURANT---------------------------------
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

    //--------------------------DAILY_SCHEDULE---------------------------------
    @Provides
    static GetDailyScheduleInteractor provideGetDailySchedule(){
        return new GetDailyScheduleInteractor(provideRestaurantDataSource());
    }

    @Provides
    static AddDailyScheduleToRestaurantInteractor provideAddDailyScheduleToRestaurant(){
    return new AddDailyScheduleToRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static DeleteDailyScheduleFromRestaurantInteractor provideDeleteDailyScheduleForRestaurant(){
        return new DeleteDailyScheduleFromRestaurantInteractor(provideRestaurantDataSource());
    }

    //--------------------------INTERESTED_USER---------------------------------
    @Provides
    static GetAllInterestedUsersForRestaurantInteractor provideGetAllInterestedUsersForRestaurant(){
        return new GetAllInterestedUsersForRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static AddCurrentUserToRestaurantInteractor provideAddCurrentUserToRestaurant(){
        return new AddCurrentUserToRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static DeleteCurrentUserFromRestaurantInteractor provideDeleteCurrentUserFromRestaurant(){
        return new DeleteCurrentUserFromRestaurantInteractor(provideRestaurantDataSource());
    }

    //--------------------------GLOBAL_USER---------------------------------
    @Provides
    static GetGlobalCurrentUserInteractor provideGetGlobalCurrentUser(){
        return new GetGlobalCurrentUserInteractor(provideRestaurantDataSource());
    }

    @Provides
    GetAllGlobalUsersInteractor provideGetAllGlobalUsers (){
        return new GetAllGlobalUsersInteractor(provideRestaurantDataSource());
    }
    @Provides
    static AddCurrentUserToGlobalListInteractor provideAddCurrentUserToGlobalList(){
        return new AddCurrentUserToGlobalListInteractor(provideRestaurantDataSource());
    }

    @Provides
    static DeleteUserFromGlobalListInteractor provideDeleteUserFromGlobalList(){
        return new DeleteUserFromGlobalListInteractor(provideRestaurantDataSource());
    }
}
