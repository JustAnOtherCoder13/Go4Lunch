package com.picone.go4lunch.presentation.di;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@InstallIn(ActivityComponent.class)
@Module
public final class PresentationModule {

    @Provides
    static FirebaseAuth provideFirebaseAuth (){return FirebaseAuth.getInstance();}
}
