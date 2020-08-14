package com.picone.core.di;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.DataBase;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
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
import static com.picone.core.data.mocks.Generator.USERS;

@InstallIn(ActivityComponent.class)
@Module
public final class coreModule {

    @Singleton
    @Provides
    static FirebaseDatabase provideFireBaseDataBase(){return FirebaseDatabase.getInstance();}

    @Singleton
    @Provides
    static UserRepository provideUserDataSource(){return new UserRepository(provideFireBaseDataBase());}

    @Provides
    static UserDaoImpl provideUserDaoImpl(){return new UserDaoImpl(provideFireBaseDataBase());}

    //generator
    @Provides
    static List<User> provideGenerateUsers() { return new ArrayList<>(USERS); }

    @Provides
    static List<Restaurant> provideGenerateRestaurant() {
        return new ArrayList<>(RESTAURANTS);
    }

    //interactors

    @Provides
    static GetAllUsers provideGetAllUsers(){return new GetAllUsers(provideGenerateUsers(),provideUserDataSource());}

    @Provides
    static GetUser provideGetUser () {return new GetUser(provideGenerateUsers());}

    @Provides
    static AddUser provideAddUser () {return new AddUser(provideUserDataSource());}

    @Provides
    static GetAllRestaurants provideGetAllRestaurants() {return new GetAllRestaurants(provideGenerateRestaurant());}

    @Provides
    static GetRestaurant provideGetRestaurant(){return new GetRestaurant(provideGenerateRestaurant());}
}
