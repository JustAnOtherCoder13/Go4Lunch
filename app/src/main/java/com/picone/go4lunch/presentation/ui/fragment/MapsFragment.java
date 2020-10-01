package com.picone.go4lunch.presentation.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.picone.core.domain.entity.Restaurant;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentMapsBinding;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.picone.go4lunch.presentation.ui.utils.GetBitmapFromVectorUtil.getBitmapFromVectorDrawable;


public class MapsFragment extends BaseFragment implements OnMapReadyCallback {

    private FragmentMapsBinding mBinding;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private final int REQUEST_CODE = 13700;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapsBinding.inflate(inflater, container, false);
        initMapView(savedInstanceState);
        showAppBars(true);
        fetchLastLocation();
        if (mAuth.getCurrentUser() != null) {
            mRestaurantViewModel.initRestaurants(mAuth.getCurrentUser().getEmail());
            mRestaurantViewModel.initUsers(mAuth.getCurrentUser().getEmail());
        }
        mBinding.locationFab.setOnClickListener(v -> setUpMapCurrentPosition());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
        if (this.getView() != null)
            initCustomMarker();
    }

    private void initMapView(@Nullable Bundle savedInstanceState) {
        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.onResume();
        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.requireContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        }
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]
                    {ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                mCurrentLocation = location;
                mBinding.mapView.getMapAsync(this);
            }
        });
    }

    private void setUpMapCurrentPosition() {
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        playLoadingAnimation(false, mAnimationView);
    }

    private void updateLocationUI() {
        if (mMap == null) return;
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (mLocationPermissionGranted) {
                setUpMapCurrentPosition();
            } else {
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void initCustomMarker() {
        mRestaurantViewModel.getAllRestaurants.observe(getViewLifecycleOwner(), restaurants -> {
            for (Restaurant restaurant : restaurants) {
                LatLng restaurantLatLng = new LatLng
                        (restaurant.getRestaurantPosition().getLatitude()
                                , restaurant.getRestaurantPosition().getLongitude());

                MarkerOptions customMarkerOption = new MarkerOptions()
                        .position(restaurantLatLng)
                        .title(restaurant.getName());

                if (restaurant.getNumberOfInterestedUsers() > 0) {
                    mMap.addMarker(customMarkerOption
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_restaurant_with_user))));
                } else {
                    mMap.addMarker(customMarkerOption
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_restaurant_with_no_user))));
                }
            }
            mMap.setOnMarkerClickListener(marker -> {
                mRestaurantViewModel.initSelectedRestaurant(marker.getTitle());
                Navigation.findNavController(requireView()).navigate(R.id.restaurantDetailFragment);
                return false;
            });
        });
    }
}