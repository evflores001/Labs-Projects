package com.cbwise1997.udrop;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.cbwise1997.udrop.Friends.FriendsActivity;
import com.cbwise1997.udrop.Locations.Loc;
import com.cbwise1997.udrop.Locations.LocationsActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;

    //widgets
    private ImageView mMyLocationImgVw;
    private ImageView mSaveLocationImgVw;
    private ImageView mCancelImgVw;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFLPC;
    private MarkerOptions mPinDrop;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private Bundle mExtras;
    private SharedPreferences mUserPrefs;
    private UserInfo mUserInfo;
    private String mUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMyLocationImgVw = (ImageView) findViewById(R.id.maps_my_location_imgvw);
        mSaveLocationImgVw = (ImageView) findViewById(R.id.maps_save_location_imgvw);
        mCancelImgVw = (ImageView) findViewById(R.id.maps_cancel_imgvw);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("users");
        mUID = mAuth.getUid();
        mExtras = getIntent().getExtras();

        getLocationPermission();
    }


    // initializes google map and calls init()
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // if permission granted then set camera to device location and drop pin
        if(mLocationPermissionGranted){
            getDeviceLocation();
            init();
        }
    }


    // checking for permissions granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; ++i){
                        // permission was not granted
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }

                        // permission granted so initialize map
                        mLocationPermissionGranted = true;
                        initMap();
                    }
                }
            }
        }
    }


    // initializes listeners and other things for the maps activity
    private void init(){
        Log.d(TAG, "init: initializing");

        initAutocomplete();

        getUserInfo();

        // moves camera and drops pin at current user location
        mMyLocationImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked location icon");
                getDeviceLocation();
            }
        });

        // saves location to database and returns to locations activity
        mSaveLocationImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExtras.containsKey("NEW_LOCATION")){
                    // save location to database
                    saveNewLocation();
                }
                else if(mExtras.containsKey("ONE_USE_LOCATION")){
                    // placeholder
                    openFriendsActivity();
                }
                else{
                    // something went wrong and user is stuck!
                    Toast.makeText(MapsActivity.this, "You are stuck here! \nSorry but you may need to relaunch.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // cancel image click listener that returns to locations activity without storing new location on database
        mCancelImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Cancelling", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapsActivity.this, LocationsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        // map click listener that drops pin where user clicks
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mPinDrop = new MarkerOptions()
                        .position(latLng)
                        .title("My Marker");
                mMap.addMarker(mPinDrop);
                Log.d(TAG, "onMapClick: New marker at lat: " + latLng.latitude + " long: " + latLng.longitude);
            }
        });
    }


    // initializes google map
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_map_fragment);
        mapFragment.getMapAsync(this);
    }

    // initializes google places autocomplete search bar
    private void initAutocomplete(){
        Places.initialize(getApplicationContext(),"AIzaSyA8uK7mI8Ytnj-ZA24QNFoPqwUphUH7p9Y");

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.maps_autocomplete_fragment);

        // Set search bar background color to white
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            // move camera to place selected from the autocomplete suggestions list
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Selected address: " + place.getAddress());
                Log.d(TAG, "Selected latitude: " + place.getLatLng().latitude);
                Log.d(TAG, "Selected longitude: " + place.getLatLng().longitude);

                moveCamera(place.getLatLng(), DEFAULT_ZOOM,"My Pin");
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    // gets current location of device and moves camera to location
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting current device location");

        mFLPC = LocationServices.getFusedLocationProviderClient(this);

        try{
            // checking for permission to access device location
            if(mLocationPermissionGranted){
                // getting location
                Task location = mFLPC.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        // success
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            Log.d(TAG, "onComplete: lat: " + currentLocation.getLatitude() + " lng: " + currentLocation.getLongitude());
                            moveCamera( new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }

                        // failure
                        else {
                            Log.d(TAG, "onComplete: unable to get current location");
                            Toast.makeText(MapsActivity.this, "Unable to get current location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }


    // if permissions have not already been granted, a permissions dialog will appear
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){

                // permissions granted so initialize map
                mLocationPermissionGranted = true;
                initMap();
            }
        }
        else {
            // prompt user to allow permissions
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    // moves camera and drops pin at given LatLng
    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving to lat: " + latLng.latitude + "lng: " + latLng.longitude);
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mPinDrop = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(mPinDrop);
    }


    // save new location to database and return to locations activity
    public void saveNewLocation(){
        final String name = mExtras.getString("NEW_LOCATION");
        final double latitude = mPinDrop.getPosition().latitude;
        final double longitude = mPinDrop.getPosition().longitude;
        Loc newLoc = new Loc(name,latitude,longitude);

        mRef.child(mUID + "/savedLocations/" + newLoc.getName()).setValue(newLoc);

        openLocationsActivity();
    }

    // retrieves userInfo from database and stores in member variable
    public void getUserInfo() {
        mRef.child(mUID + "/userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserInfo = dataSnapshot.getValue(UserInfo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openLocationsActivity(){
        Intent intent = new Intent(MapsActivity.this, LocationsActivity.class);
        finish();
        startActivity(intent);
    }

    public void openFriendsActivity() {
        Intent intent = new Intent(MapsActivity.this, FriendsActivity.class);
        finish();
        startActivity(intent);
    }
}