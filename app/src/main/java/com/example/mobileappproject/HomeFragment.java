package com.example.mobileappproject;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
*   Home Fragment is now home to the Google Map feature.
*
 */



public class HomeFragment extends Fragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "HomeFragment";

    private static final int REQUEST_LOCATION_CODE = 100;
    private int PROXIMITY_RADIUS = 10000;

    MapView mapView;
    private GoogleMap googleMap;

    private GoogleApiClient client;

    private LocationRequest locationRequest;

    private Marker currentLocationMarker;
    private Location lastLocation;


    public HomeFragment(){
        Log.d(TAG, "constructed.");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "created.");


        View view = inflater.inflate(R.layout.fragment_home, container, false);

/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();

        fragment.getMapAsync(this); */



        //Check permissions
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i(TAG, "onCreateView: Permissions not granted. Requesting permission...");

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
        }
        else {

            Log.i(TAG, "onCreateView: Permissions granted.");

            mapView = (MapView) view.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            mapView.onResume();

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                Toast toast = new Toast(getContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setText("Exception");
                toast.show();
            }

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    Log.i(TAG, "onMapReady: Called");

                    googleMap = mMap;
                    googleMap.setMyLocationEnabled(true);

                    LatLng sydney = new LatLng(-34, 151);

                    //googleMap.getMyLocation();

                    googleMap.addMarker(new MarkerOptions().position(sydney)
                            .title("title").snippet("snip"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });

        }
            return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "onConnectionSuspended: ");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ");

    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();

    }

    private String getUrl(double latitude, double longitude, String location){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json");
        googlePlaceUrl.append("location" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + location);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key= AIzaSyCgoXDzopQsZJ-GmH_iFCz3ByRkYjqzUeA");

        return googlePlaceUrl.toString();

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentLocationMarker = googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

}
