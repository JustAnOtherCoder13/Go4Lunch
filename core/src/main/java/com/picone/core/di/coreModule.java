package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.RestaurantDaoImpl;
import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.interactors.restaurantsInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetFanListForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForKeyInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GooglePlaceInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateFanListForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateNumberOfInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetInterestedUsersForRestaurantKeyInteractor;

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


    //-----------------------------------DATA SOURCE------------------------------------------------
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
    static RestaurantRepository provideRestaurantDataSource(){
        return new RestaurantRepository(provideFireBaseDataBase(), provideRestaurantDaoImpl());
    }

    //--------------------------------------DAO-----------------------------------------------------
    @Provides
    static UserDaoImpl provideUserDaoImpl() {
        return new UserDaoImpl(provideFireBaseDataBase());
    }

    @Provides
    static RestaurantDaoImpl provideRestaurantDaoImpl(){
        return new RestaurantDaoImpl(provideFireBaseDataBase());
    }

    //-----------------------------------GENERATOR--------------------------------------------------
    @Provides
    static List<Restaurant> provideGenerateRestaurant() {
        return new ArrayList<>(RESTAURANTS);
    }

    //--------------------------------USERS INTERACTORS---------------------------------------------
    @Provides
    static GetAllUsersInteractor provideGetAllUsers() {
        return new GetAllUsersInteractor(provideUserDataSource());
    }

    @Provides
    static AddUserInteractor provideAddUser() {
        return new AddUserInteractor(provideUserDataSource());
    }

    @Provides
    static GetCurrentUserForEmailInteractor provideGetUserForEmail(){
        return new GetCurrentUserForEmailInteractor(provideUserDataSource());
    }

    @Provides
    static UpdateUserChosenRestaurantInteractor provideUpdateUserChosenRestaurant(){
        return new UpdateUserChosenRestaurantInteractor(provideRestaurantDataSource());
    }

    //-----------------------------RESTAURANTS INTERACTORS------------------------------------------
    @Provides
    static GetAllRestaurantsInteractor provideGetAllRestaurants() {
        return new GetAllRestaurantsInteractor(provideRestaurantDataSource(),provideGenerateRestaurant());
    }

    @Provides
    static GetRestaurantInteractor provideGetRestaurant() {
        return new GetRestaurantInteractor(provideRestaurantDataSource(),provideGenerateRestaurant());
    }

    @Provides
    static AddRestaurantInteractor provideAddRestaurant(){
        return new AddRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetRestaurantForNameInteractor provideGetRestaurantForName(){
        return new GetRestaurantForNameInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetRestaurantForKeyInteractor provideGetRestaurantForKey(){
        return new GetRestaurantForKeyInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetInterestedUsersForRestaurantKeyInteractor provideGetInterestedUsersForRestaurantKey(){
        return  new GetInterestedUsersForRestaurantKeyInteractor(provideUserDataSource());
    }

    @Provides
    static UpdateNumberOfInterestedUsersForRestaurantInteractor provideUpdateNumberOfInterestedUsersForRestaurant(){
        return new UpdateNumberOfInterestedUsersForRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetAllPersistedRestaurantsInteractor provideGetAllPersistedRestaurant(){
        return new GetAllPersistedRestaurantsInteractor(provideRestaurantDataSource());
    }

    @Provides
    GetFanListForRestaurantInteractor provideGetFanListForRestaurant(){
        return new GetFanListForRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    UpdateFanListForRestaurantInteractor provideUpdateFanListForRestaurant(){
        return new UpdateFanListForRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GooglePlaceInteractor provideGetRetrofitClient(){
        return new GooglePlaceInteractor(provideRestaurantDataSource());
    }
}
