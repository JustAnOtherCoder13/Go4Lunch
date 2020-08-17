package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.RestaurantDaoImpl;
import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.interactors.AddUser;
import com.picone.core.domain.interactors.GetAllRestaurants;
import com.picone.core.domain.interactors.GetAllUsers;
import com.picone.core.domain.interactors.GetRestaurant;
import com.picone.core.domain.interactors.GetUser;

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
    static GetAllUsers provideGetAllUsers() {
        return new GetAllUsers(provideUserDataSource());
    }

    @Provides
    static GetUser provideGetUser() {
        return new GetUser(provideUserDataSource());
    }

    @Provides
    static AddUser provideAddUser() {
        return new AddUser(provideUserDataSource());
    }

    //restaurant interactors
    @Provides
    static GetAllRestaurants provideGetAllRestaurants() {
        return new GetAllRestaurants(provideRestaurantDataSource());
    }

    @Provides
    static GetRestaurant provideGetRestaurant() {
        return new GetRestaurant(provideRestaurantDataSource());
    }
}
