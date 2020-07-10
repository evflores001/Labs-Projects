package com.cbwise1997.udrop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
/*
public class FavoritesActivity extends FragmentActivity implements OnMapReadyCallback {
    private String PROFILE_PREFS = "ProfilePrefs";
    private String PLACE_NAME = "PlaceName";
    private String USER_ID_KEY = "UserIDKey";

    private String mUserID;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    private LatLng California = new LatLng(36, 119);
  //  private static final String TAG = LocationsActivity.MapsMessengerActivityCurrentPlace.class.getSimpleName();
    private Button mBackBtn, mAddBtn;
    private ArrayList<UserInfo> mFavorites = new ArrayList<>();
    private RecyclerView mLocationsRV;
    private RecyclerView mFavoritessRV;
    private EditText mPlace_ET;
    private GoogleMap mMap;
    boolean mLocationPermissionGranted;
    private FirebaseAuth mAuth;
    private Location mLastKnownLocation;
    private LocationRequest locationRequest;
    public LatLng mDefaultLocatio;
    private FusedLocationProviderClient mFusedProviderClient;
    private final float DEFAULT_ZOOM = 15;
    private LocationCallback mLocationCallback;
    private DatabaseReference mDatabaseReference;

    private DatabaseReference mUsersRef;

    public class MapsMessengerActivityCurrentPlace extends AppCompatActivity
            implements OnMapReadyCallback{
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFields);
        @Override
        public void onMapReady(GoogleMap googleMap) {

        }

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mFavorites = new ArrayList<>();
        mPlace_ET = findViewById(R.id.place_ET);

        final Bundle mEmail = getIntent().getExtras();
        if (mEmail != null) {
            String emailToMessage = mEmail.getString("mEmail");
            //The key argument here must match that used in the other activity
            toaster(emailToMessage + " Test");
        }
        mFusedProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };
        mAuth = FirebaseAuth.getInstance();
        mBackBtn = findViewById(R.id.mBack);
        mAddBtn = findViewById(R.id.mAdd);
        mUsersRef = FirebaseDatabase.getInstance().getReference("users");


        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomepageActivity();
            }
        });
        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
    }


    private void getDeviceLocation() {// ERROR BELOW BC IT HAS NOT BEEN CALLED YET
//        Toast.makeText(getApplicationContext(),mLastKnownLocation.getLongitude() +" : " + mLastKnownLocation.getLatitude(),Toast.LENGTH_SHORT).show();

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
/*
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedProviderClient.getLastLocation();                   // TTY THIS IF NO WORKKrequestLocationUpdates();
                //requestLocationUpdates(locationRequest.create(),mLocationCallback, Looper.getMainLooper()  ); //.requestLocationUpdates();
//                Toast.makeText(getApplicationContext(),mLastKnownLocation.getLongitude() +" : " + mLastKnownLocation.getLatitude(),Toast.LENGTH_SHORT).show();

                // Toast.makeText(getApplicationContext(),mFusedProviderClient.getLastLocation()+" : ",Toast.LENGTH_SHORT).show();

                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            // Set the map's camera position to the current location of the device.
                            //    Toast.makeText(getApplicationContext(),task.getResult()+" : ",Toast.LENGTH_SHORT).show();

                            mLastKnownLocation = (Location) task.getResult();
                            // mLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            assert mLastKnownLocation != null;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            Toast.makeText(getApplicationContext(),mLastKnownLocation.getLatitude() +" : " + mLastKnownLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(California, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG,"ali mde it to reequest permissions result");

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    protected void createLocationRequest() {
//        Toast.makeText(getApplicationContext(),"Ali made ONE locaiton Request",Toast.LENGTH_SHORT).show();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void updateLocationUI( ) {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                Log.d(TAG,"ali mde it to updatelocation granted");

            } else {
                Log.d(TAG,"ali mde it to updatelocation denied");

                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                Object mLastKnownLocation = null;
                }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void addPlace(double Latitude, double Longitude){
        DatabaseReference userRef = mUsersRef.child("users");
        final String place = mPlace_ET.getText().toString().trim();
        final Double latitude = Latitude;
        final Double longitude = Longitude;

//        SharedPreferences prefs = getSharedPreferences(PROFILE_PREFS,0);

        mUserID = getSharedPreferences(PROFILE_PREFS, 0).getString(USER_ID_KEY, "error");
        toaster(place + latitude + longitude + mUserID+ " Test");

/*
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Log.d("Udrop","createUser() onComplete: " + task.isSuccessful());
                    // failed to create new user
                    if(!task.isSuccessful()){
                        showErrorDialog("Sign up failed");
                        Log.d("Udrop","Error: " + task.getException());
                    }
                    // successfully created new user
                    else{
                        // save user info to prefs and database then move to Login Activity
                        Log.d("Udrop","User ID: " + mAuth.getUid());
                        Log.d("Udrop","User name: " + firstName + " " + lastName);
                        Log.d("Udrop","User nickname: " + nickname);
                        Log.d("Udrop","User email: " + email);
                        Log.d("Udrop","User phone: " + phone);

                        mDatabaseReference.child(userID).child("userInfo").setValue(newUser);
                        openHomepageActivity();
                    }
                }


        else {
            Toast.makeText(this,"One or more sign up fields are invalid...",Toast.LENGTH_SHORT).show();
        }
*/
/*
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            //mMap.updateLocationUI();

            @Override
            public void onMapClick(LatLng position) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(position));
                final double Longitude = position.longitude;
                final double Latitude = +position.latitude;
                Toast.makeText(getApplicationContext(),Longitude +" : "+ Latitude,Toast.LENGTH_SHORT).show();
                mAddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPlace(Latitude, Longitude);
                        // ADD NEW FAVORITE LOCATION HERE();
                    }
                });
            }
        });
    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
/*
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    private void toaster(String emailToMessage) {
        Toast.makeText(this, emailToMessage, Toast.LENGTH_SHORT).show();
    }




    private void openHomepageActivity() {
        Intent intent = new Intent(FavoritesActivity.this, HomepageActivity.class);
        finish();
        startActivity(intent);
    }
}
*/
