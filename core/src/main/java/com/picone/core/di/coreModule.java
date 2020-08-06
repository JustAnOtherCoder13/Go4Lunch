package com.picone.core.di;

import com.picone.core.data.repository.FirebaseDataBase;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.interactors.GetAllUsers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@InstallIn(ActivityComponent.class)
@Module
public final class coreModule {

    @Singleton
    @Provides
    static FirebaseDataBase provideFirebaseDatabase () {return FirebaseDataBase.getInstance();}

    @Singleton
    @Provides
    static UserRepository provideUserDataSource(){return new UserRepository();}

}
