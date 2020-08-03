interface Versions{
    String appCompat = "1.1.0";
    String jUnit = "4.13";
    String lifeCycle = "2.2.0";
    String navigationComponent = "2.3.0";
    String facebook = "4.42.0";
    String playService = "17.0.0";
}

class Androidx {
    public static String appcompat = "androidx.appcompat:appcompat:"+Versions.appCompat;
    public static String constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3";
    public static String legacySupport = "androidx.legacy:legacy-support-v4:1.0.0";
}
class Testing {
    public static String jUnit = "junit:junit:"+Versions.jUnit;
    public static String jUnitAndroidx = "androidx.test.ext:junit:1.1.1";
    public static String espresso = "androidx.test.espresso:espresso-core:3.2.0";
}

class RecyclerViewDependency {
    public static String recyclerView = "androidx.recyclerview:recyclerview:1.1.0";

}
class LifecycleDependency {
    public static String viewModel = "androidx.lifecycle:lifecycle-viewmodel:"+Versions.lifeCycle;
    public static String liveData = "androidx.lifecycle:lifecycle-livedata:"+Versions.lifeCycle;
    public static String common = "androidx.lifecycle:lifecycle-common-java8:"+Versions.lifeCycle;
    public static String viewModelSavedStates = "androidx.lifecycle:lifecycle-viewmodel-savedstate:"+Versions.lifeCycle;
    public static String lifeCycle = "androidx.lifecycle:lifecycle-extensions:"+Versions.lifeCycle;
}
class NavigationcomponentDependency {

    public static String navigationFragment = "androidx.navigation:navigation-fragment:"+Versions.navigationComponent;
    public static String navigationUi = "androidx.navigation:navigation-ui:"+Versions.navigationComponent;
    public static String dynamicFeatures = "androidx.navigation:navigation-dynamic-features-fragment:"+Versions.navigationComponent;
    public static String navigationTesting = "androidx.navigation:navigation-testing:"+Versions.navigationComponent;
}
class FirebaseDependency {
    public static String core = "com.google.firebase:firebase-core:17.4.4";
    public static String fireAuth = "com.google.firebase:firebase-auth:19.3.2";
    public static String fireStore = "com.google.firebase:firebase-firestore:21.5.0";
}

class GoogleauthDependency {
    public static String googleAuth = "com.google.android.gms:play-services-auth:18.1.0";
}
class FacebookauthDependency {
    public static String facebookSdk = "com.facebook.android:facebook-android-sdk:"+Versions.facebook;
    public static String facebookLogin = "com.facebook.android:facebook-login:"+Versions.facebook;
}
class GooglemapsDependency {
    public static String maps = "com.google.android.gms:play-services-maps:"+Versions.playService;
    public static String mapsLocation = "com.google.android.gms:play-services-location:"+Versions.playService;
    public static String places = "com.google.android.libraries.places:places:2.3.0";
}

