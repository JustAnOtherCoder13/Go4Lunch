package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.place.RetrofitClient;
import com.picone.core.data.repository.restaurant.RestaurantDaoImpl;
import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.data.repository.user.UserDaoImpl;
import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.interactors.SendNotificationInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantDetailInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import retrofit2.http.POST;

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
    static RestaurantRepository provideRestaurantDataSource() {
        return new RestaurantRepository(provideFireBaseDataBase(), provideRestaurantDaoImpl());
    }

    //--------------------------------------DAO-----------------------------------------------------
    @Provides
    static UserDaoImpl provideUserDaoImpl() {
        return new UserDaoImpl(provideFireBaseDataBase());
    }

    @Provides
    static RestaurantDaoImpl provideRestaurantDaoImpl() {
        return new RestaurantDaoImpl(provideFireBaseDataBase(), provideRetrofitClient());
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
    static GetCurrentUserForEmailInteractor provideGetUserForEmail() {
        return new GetCurrentUserForEmailInteractor(provideUserDataSource());
    }

    @Provides
    static UpdateUserChosenRestaurantInteractor provideUpdateUserChosenRestaurant() {
        return new UpdateUserChosenRestaurantInteractor(provideRestaurantDataSource());
    }

    //-----------------------------RESTAURANTS INTERACTORS------------------------------------------

    @Provides
    static AddRestaurantInteractor provideAddRestaurant() {
        return new AddRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetAllPersistedRestaurantsInteractor provideGetAllPersistedRestaurant() {
        return new GetAllPersistedRestaurantsInteractor(provideRestaurantDataSource());
    }

    //-----------------------------PLACE INTERACTORS------------------------------------------

    @Provides
    static FetchRestaurantFromPlaceInteractor provideFetchRestaurantFromPlace() {
        return new FetchRestaurantFromPlaceInteractor(provideRestaurantDataSource());
    }

    @Provides
    static FetchRestaurantDetailFromPlaceInteractor provideFetchRestaurantDetailFromPlace() {
        return new FetchRestaurantDetailFromPlaceInteractor(provideRestaurantDataSource());
    }

    @Provides
    static FetchRestaurantDistanceInteractor provideFetchRestaurantDistance() {
        return new FetchRestaurantDistanceInteractor(provideRestaurantDataSource());
    }

    @Provides
    static RetrofitClient provideRetrofitClient() {
        return new RetrofitClient();
    }

    @Provides
    static GetPredictionInteractor provideGetPredictions() {
        return new GetPredictionInteractor(provideRestaurantDataSource());
    }

    @Provides
    static SendNotificationInteractor provideSendNotification(){
        return new SendNotificationInteractor(provideRestaurantDataSource());
    }
}
