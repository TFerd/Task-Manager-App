package com.example.mobileappproject;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static final float DEFAULT_ZOOM = 15f;

    MapView mapView;
    private GoogleMap googleMap;

    private GoogleApiClient client;

    private LocationRequest locationRequest;

    private Marker currentLocationMarker;
    private Location lastLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private double latitude, longitude;

    //private ImageButton searchButton;
    private EditText searchText;

    private ImageView centerGPS;

    private Button addLocationButton;

    //private String apiKey = getString(R.string.api_key);




    public HomeFragment(){
        Log.d(TAG, "constructed.");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "created.");


        final View view = inflater.inflate(R.layout.fragment_home, container, false);



        if(!Places.isInitialized()){
            Log.i(TAG, "onCreateView: Places initialized.");
            Places.initialize(getContext(), "AIzaSyBuLxjCEfnGwKTPOgiClUB_J4WCec_zApI");
        }

        final PlacesClient placesClient = Places.createClient(getContext());


        /*
        The Autocomplete section below is pretty much the whole search bar. When you select a place, it will return a Place object
        which you can then use however.
         */
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
        getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {
                Log.i(TAG, "onPlaceSelected: " + place.getName() + ", " + place.getId());

                moveCamera(place.getLatLng(), DEFAULT_ZOOM, place.getName());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Assign to a task?");
                builder.setMessage("Would you like to add this location to a task?");
                builder.setCancelable(true);

                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addTaskDialog(place);
                    }
                });

                AlertDialog addLocDialog = builder.create();
                addLocDialog.show();

                //addTaskDialog(place);

            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "onError: Error");
            }
        });



        //searchText = (EditText) view.findViewById(R.id.location_search_box);
        centerGPS = (ImageView) view.findViewById(R.id.ic_gps);


        centerGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: clicked on gps icon");

                getMyLocation();
                //placesClient.findCurrentPlace();
            }
        });



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
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                    getMyLocation();

                }
            });

        }
            return view;
    }

    private void getMyLocation(){
        Log.i(TAG, "getMyLocation: called");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        
        try{
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Log.i(TAG, "onComplete: Success");
                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                DEFAULT_ZOOM, "My Location");
                    }
                    else{
                        Log.i(TAG, "onComplete: Could not find current location");
                    }
                }
            });
        }catch (SecurityException e){
            Log.e(TAG, "getMyLocation: ", e.getCause());
        }


    }

    private void findLocation(){

        Log.i(TAG, "findLocation: Called");

        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(list.size() > 0){
            Address address = list.get(0);

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));

            Log.i(TAG, "findLocation: Found location: " + address.toString());
        }
        else {
            Log.i(TAG, "findLocation: No location found :(");
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.i(TAG, "moveCamera: moving to: Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);

        googleMap.clear();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //if(!title.equals("My Location")) {

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            googleMap.addMarker(markerOptions);
        //}

        hideKeyboard();
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
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?");
        googlePlaceUrl.append("location" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + location);
        //googlePlaceUrl.append("&keyword=" + location);
        googlePlaceUrl.append("&sensor=true");
        //googlePlaceUrl.append("&key= AIzaSyCgoXDzopQsZJ-GmH_iFCz3ByRkYjqzUeA");
        googlePlaceUrl.append("&key=AIzaSyBuLxjCEfnGwKTPOgiClUB_J4WCec_zApI");

        return googlePlaceUrl.toString();

    }

    private String directionsURL(String originID, String destinationID){
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=place_id:" + originID);
        googleDirectionsUrl.append("&destination=place_id:" + destinationID);
        googleDirectionsUrl.append("&key=" + "AIzaSyBuLxjCEfnGwKTPOgiClUB_J4WCec_zApI");

        return googleDirectionsUrl.toString();
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


    private void hideKeyboard(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void addTaskDialog(Place place){

        Log.i(TAG, "addTaskDialog: called.");
        
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_location_add_dialog);
        dialog.setTitle("Select task to assign location.");

        String locationId = place.getId();


        ArrayList<String> taskList = new ArrayList<>();

        ListView listView = dialog.findViewById(R.id.location_task_listview);

        final DBSQLiteOpenHelper db = new DBSQLiteOpenHelper(getContext());

        taskList = fillTasksString(db);

        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.task_name, taskList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = db.getAllData();

                //MyTask myTask = (MyTask)arrayAdapter.getItem(position);

                //Log.i(TAG, "onItemClick: Task ID: " + myTask.getId());

                Log.i(TAG, "onItemClick: Position number: " + position + " clicked.");

                dialog.dismiss();

            }
        });

        dialog.setCancelable(true);
        dialog.show();

    }

    private ArrayList<MyTask> fillTasks(DBSQLiteOpenHelper db) {
        ArrayList<MyTask> myTasks = new ArrayList<>();
        Cursor cursor = db.getAllData();
        if (cursor.getCount() < 1) {
            return myTasks;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String desc = cursor.getString(2);
            int hour = cursor.getInt(3);
            int minute = cursor.getInt(4);
            int month = cursor.getInt(5);
            int day = cursor.getInt(6);
            int year = cursor.getInt(7);
            boolean notify = cursor.getInt(8) > 0;
            boolean complete = cursor.getInt(9) > 0;
            String locId = cursor.getString(0);

            MyTask myTask =
                    new MyTask(id, name, desc, notify, hour, minute, month, day,
                            year);
            if (complete) {
                myTask.setComplete();
            } else
                myTask.setIncomplete();

            myTasks.add(myTask);

        }
        return myTasks;
    }


    private ArrayList<String> fillTasksString(DBSQLiteOpenHelper db){
        ArrayList<String> tasks = new ArrayList<>();

        Cursor cursor = db.getAllData();
        if (cursor.getCount() < 1) {
            return tasks;
        }

        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            tasks.add(name);
        }
        return tasks;
    }


}
