package com.picone.core.di;

import com.picone.core.data.repository.FirebaseDataBase;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

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
    static FirebaseDataBase provideFirebaseDatabase () {return FirebaseDataBase.getInstance();}

    @Singleton
    @Provides
    static UserRepository provideUserDataSource(){return new UserRepository();}

    @Provides
    static List<User> provideGenerateUsers() { return new ArrayList<>(USERS); }

    @Provides
    static List<Restaurant> provideGenerateRestaurant() {
        return new ArrayList<>(RESTAURANTS);
    }
}
