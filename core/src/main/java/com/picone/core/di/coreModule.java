package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.RestaurantDaoImpl;
import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.DeleteRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetAllGeneratedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetGeneratedRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetPersistedRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.userForRestaurant.AddCurrentUserToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.userForRestaurant.DeleteCurrentUserFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.userForRestaurant.GetAllInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;

import java.util.ArrayList;

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
    static GetAllGeneratedRestaurantsInteractor provideGetAllGeneratedRestaurants() {
        return new GetAllGeneratedRestaurantsInteractor(new ArrayList<>(RESTAURANTS));
    }

    @Provides
    static GetAllPersistedRestaurantsInteractor provideGetAllPersistedRestaurant(){
        return new GetAllPersistedRestaurantsInteractor(provideRestaurantDataSource());
    }

    @Provides
    static GetGeneratedRestaurantInteractor provideGetGeneratedRestaurant() {
        return new GetGeneratedRestaurantInteractor(new ArrayList<>(RESTAURANTS));
    }

    @Provides
    static GetPersistedRestaurantInteractor provideGetPersistedRestaurant(){
        return new GetPersistedRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static AddRestaurantInteractor provideAddRestaurant(){
        return  new AddRestaurantInteractor(provideRestaurantDataSource());
    }

    @Provides
    static DeleteRestaurantInteractor provideDeleteRestaurant(){
        return new DeleteRestaurantInteractor(provideRestaurantDataSource());
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
}
