package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.chat.ChatMessageDaoImpl;
import com.picone.core.data.repository.chat.ChatMessageRepository;
import com.picone.core.data.repository.restaurant.RestaurantDaoImpl;
import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.data.repository.services.RetrofitClient;
import com.picone.core.data.repository.user.UserDaoImpl;
import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.interactors.SendNotificationInteractor;
import com.picone.core.domain.interactors.chatInteractors.GetAllMessagesInteractor;
import com.picone.core.domain.interactors.chatInteractors.PostMessageInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserDailySchedulesInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

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
        return new UserRepository(provideUserDaoImpl());
    }

    @Singleton
    @Provides
    static RestaurantRepository provideRestaurantDataSource() {
        return new RestaurantRepository(provideRestaurantDaoImpl());
    }

    @Singleton
    @Provides
    static ChatMessageRepository provideChatMessageDataSource() {
        return new ChatMessageRepository(provideChatMessageDaoImpl());
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

    @Provides
    static ChatMessageDaoImpl provideChatMessageDaoImpl() {
        return new ChatMessageDaoImpl(provideFireBaseDataBase());
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
    static SendNotificationInteractor provideSendNotification() {
        return new SendNotificationInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetCurrentUserDailySchedulesInteractor provideGetCurrentUserDailyScheduleOnToday() {
        return new GetCurrentUserDailySchedulesInteractor(provideUserDataSource());
    }

    //-----------------------------CHAT INTERACTORS------------------------------------------

    @Provides
    static GetAllMessagesInteractor provideGetAllMessages() {
        return new GetAllMessagesInteractor(provideChatMessageDataSource());
    }

    @Provides
    static PostMessageInteractor postMessageInteractor() {
        return new PostMessageInteractor(provideChatMessageDataSource());
    }

}
