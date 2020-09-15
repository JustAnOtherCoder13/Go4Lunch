package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.RestaurantDaoImpl;
import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.interactors.restaurantsInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetUserInteractor;

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
    static RestaurantRepository provideRestaurantDataSource(){
        return new RestaurantRepository(provideFireBaseDataBase(), provideRestaurantDaoImpl());
    }

    //DAO
    @Provides
    static UserDaoImpl provideUserDaoImpl() {
        return new UserDaoImpl(provideFireBaseDataBase());
    }

    @Provides
    static RestaurantDaoImpl provideRestaurantDaoImpl(){
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
        return new GetAllRestaurantsInteractor(provideRestaurantDataSource(),provideGenerateRestaurant());
    }

    @Provides
    static GetRestaurantInteractor provideGetRestaurant() {
        return new GetRestaurantInteractor(provideRestaurantDataSource(),provideGenerateRestaurant());
    }

    @Provides
    static GetRestaurantForNameInteractor provideGetRestaurantForName(){
        return new GetRestaurantForNameInteractor(provideRestaurantDataSource());
    }

    @Provides
    static AddRestaurantInteractor provideAddRestaurant(){
        return new AddRestaurantInteractor(provideRestaurantDataSource());
    }
}
