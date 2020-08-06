package com.picone.go4lunch.presentation.di;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.R;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;

@InstallIn(ActivityComponent.class)
@Module
public final class PresentationModule {

    @Provides
    static FirebaseAuth provideFirebaseAuth (){return FirebaseAuth.getInstance();}

    @Provides
    static GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("878466115646-ma6qfc8p40tnuvofpopcbcjkuf3f7pmj.apps.googleusercontent.com")
                .requestEmail()
                .build();
    }

    @Provides
    static GoogleSignInClient provideSignInClient (@ActivityContext Context context) {return GoogleSignIn.getClient(context, getGoogleSignInOptions());}



}


