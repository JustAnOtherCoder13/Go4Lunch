package com.picone.go4lunch.presentation.di;

import android.content.Context;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.utils.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InstallIn(ActivityComponent.class)
@Module
public final class PresentationModule {

    @Provides
    static BaseFragment provideBaseFragment() { return new BaseFragment() {}; }

    @Provides
    static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    static GoogleSignInOptions provideGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("878466115646-ma6qfc8p40tnuvofpopcbcjkuf3f7pmj.apps.googleusercontent.com")
                .requestEmail()
                .build();
    }

    @Provides
    static GoogleSignInClient provideGoogleSignInClient(@ActivityContext Context context) {
        return GoogleSignIn.getClient(context, provideGoogleSignInOptions());
    }

    @Provides
    static CallbackManager provideCallbackManager() {
        return CallbackManager.Factory.create();
    }

    @Provides
    static SchedulerProvider provideScheduler() { return new SchedulerProvider(Schedulers.io(), AndroidSchedulers.mainThread());}
}