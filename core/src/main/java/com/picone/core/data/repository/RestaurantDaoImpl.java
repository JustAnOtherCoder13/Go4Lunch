package com.picone.core.data.repository;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantPosition;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.retrofitRestaurant.NearBySearch;
import com.picone.core.domain.entity.retrofitRestaurant.Photo;
import com.picone.core.domain.entity.retrofitRestaurant.RestaurantPOJO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantDaoImpl implements RestaurantDao {

    @Inject
    protected FirebaseDatabase database;
    private DatabaseReference restaurantsDataBaseReference;
    private final String URL = "https://maps.googleapis.com/maps/";


    public RestaurantDaoImpl(FirebaseDatabase database) {
        this.database = database;
        this.restaurantsDataBaseReference = database.getReference().child("restaurants");
    }

    @Override
    public Observable<Restaurant> getRestaurantForName(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantsDataBaseReference.child(restaurantName), Restaurant.class).toObservable();
    }

    @Override
    public Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey) {
        Query query = restaurantsDataBaseReference.orderByChild("key").equalTo(restaurantKey);
        return RxFirebaseDatabase.observeSingleValueEvent(query, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        restaurant.setKey(restaurantsDataBaseReference.child(restaurant.getName()).push().getKey());
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference
                .child(restaurant.getName()), restaurant);
    }

    @Override
    public Completable updateUserChosenRestaurant(User currentUser) {
        return RxFirebaseDatabase.setValue(database.getReference().child("users").child(currentUser.getUid()), currentUser);
    }

    @Override
    public Completable updateNumberOfInterestedUsersForRestaurant(String restaurantName, int numberOfInterestedUsers) {
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference.child(restaurantName).child("numberOfInterestedUsers"), numberOfInterestedUsers);
    }

    @Override
    public Observable<List<Restaurant>> getAllPersistedRestaurants() {
        return RxFirebaseDatabase.observeValueEvent(restaurantsDataBaseReference, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    public Observable<List<Restaurant>> googleMethods(Location mCurrentLocation) {
            List<Restaurant> restaurantsFromMap = new ArrayList<>();
            GooglePlaceService googlePlaceService = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(new OkHttpClient().newBuilder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GooglePlaceService.class);

            return Observable.create(emitter -> {
                googlePlaceService
                        .getNearbySearch
                                ("restaurant"
                                        , mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude()
                                        ,700)
                        .enqueue(new Callback<NearBySearch>() {
                            @Override
                            public void onResponse(Call<NearBySearch> call, Response<NearBySearch> response) {
                                NearBySearch nearBySearch = response.body();
                                if (nearBySearch.getStatus().equals("OK")){
                                    for (RestaurantPOJO restaurantPOJO : nearBySearch.getRestaurantPOJOS()) {
                                        String name = restaurantPOJO.getName();
                                        int distance = 0;
                                        Photo photo = restaurantPOJO.getPhotos().get(0);
                                        //TODO hide apiKey
                                        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?"
                                                .concat("maxwidth="+photo.getWidth())
                                                .concat("&photoreference="+photo.getPhotoReference())
                                                .concat("&key=AIzaSyBPfbZ_poV0QGgdifNxGzHHz2yS4L2evTI");

                                        Double lat = restaurantPOJO.getGeometry().getLocation().getLat();
                                        Double lng = restaurantPOJO.getGeometry().getLocation().getLng();
                                        String address = restaurantPOJO.getVicinity();

                                        Log.i("TAG", "onResponse: "+photoUrl);
                                        Restaurant restaurant = new Restaurant(null,name,distance,photoUrl,"",address,0,0,new RestaurantPosition(lat,lng),0,new ArrayList<>());
                                        if (!restaurantsFromMap.contains(restaurant))
                                            restaurantsFromMap.add(restaurant);
                                    }
                                }
                                emitter.onNext(restaurantsFromMap);
                            }

                            @Override
                            public void onFailure(Call<NearBySearch> call, Throwable t) {
                                emitter.onError(t);
                                Log.d("onFailure", t.toString());
                            }
                        });
            });


    }

    public Completable updateFanListForRestaurant(String restaurantName,List<String> fanList){
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference.child(restaurantName).child("fanList"),fanList);
    }

    public Observable<List<String>> getFanListForRestaurant(String restaurantName){
        return  RxFirebaseDatabase.observeSingleValueEvent(restaurantsDataBaseReference.child(restaurantName).child("fanList"),DataSnapshotMapper.listOf(String.class)).toObservable();
    }
}
