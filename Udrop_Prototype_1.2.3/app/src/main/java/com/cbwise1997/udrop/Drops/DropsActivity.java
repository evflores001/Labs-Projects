package com.cbwise1997.udrop.Drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cbwise1997.udrop.Friends.FriendListAdapter;
import com.cbwise1997.udrop.Friends.FriendsActivity;
import com.cbwise1997.udrop.Locations.LocationsActivity;
import com.cbwise1997.udrop.MenuRecyclerViewAdapter;
import com.cbwise1997.udrop.Messages.Messages;
import com.cbwise1997.udrop.ProfileActivity;
import com.cbwise1997.udrop.R;
import com.cbwise1997.udrop.UserInfo;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DropsActivity extends AppCompatActivity {

    private static final String TAG = "DropsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final double STANDARD_RADIUS = 0.015; // radius is in kilometers, so 0.015 = 15 meter radius
    private static final int MAX_UPDATE_INTERVAL = 3000;
    private static final int MIN_UPDATE_INTERVAL = 2000;


    //widgets
    private RecyclerView mDropsRV;
    private RecyclerView mMenuRV;
    private TextView mDropsTitleTV;

    // variables
    private ArrayList<Messages> mDrops;
    private ArrayList<String> mMenuItems;
    private DropsAdapter mDropsAdapter;
    private RecyclerView.LayoutManager mDropsLayoutManager;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private String mUID;

    private FusedLocationProviderClient mFLPC;
    private LocationRequest mLocationRequest;

    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drops);

        init();
    }

    private void init(){
        mMenuRV = (RecyclerView) findViewById(R.id.drops_menu_recvw);
        mDropsRV = (RecyclerView) findViewById(R.id.drops_drops_recvw);
        mDropsTitleTV = (TextView) findViewById(R.id.drops_title_txtvw);

        mRef = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mUID = mAuth.getUid();
        mDrops = new ArrayList<>();
        mMenuItems = new ArrayList<>();

        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null) {
                    return;
                }
                for(final Location location : locationResult.getLocations()){
                    Log.d(TAG, "onLocationResult: " + location.toString());
                    mRef.child(mUID + "/drops").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if(Integer.parseInt(snapshot.child("status").getValue().toString()) == 1){
                                    double lat2 = Double.parseDouble(snapshot.child("latitude").getValue().toString());
                                    double long2 = Double.parseDouble(snapshot.child("longitude").getValue().toString());;
                                    LatLng latLng2 = new LatLng(lat2,long2);
                                    if(distance(latLng,latLng2) <= STANDARD_RADIUS){
                                        DatabaseReference ref = snapshot.child("status").getRef();
                                        ref.setValue(0);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        mFLPC = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(MAX_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(MIN_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        getLocationPermission();

        checkSettings();

        setDropsDatabaseListener();

        buildMenu();
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
                return;
            }
        }
        else {
            // prompt user to allow permissions
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //calculates distance between two LatLng objects
    private double distance(LatLng latLng1, LatLng latLng2){
        double theta = latLng1.longitude - latLng2.longitude;
        double dist = Math.sin(degToRad(latLng1.latitude))
                * Math.sin(degToRad(latLng2.latitude))
                + Math.cos(degToRad(latLng1.latitude))
                * Math.cos(degToRad(latLng2.latitude))
                * Math.cos(degToRad(theta));
        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double radToDeg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    // checks that phone has correct settings enabled to send location updates
    private void checkSettings(){
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();

        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if error is resolvable then try to solve it
                if(e instanceof ResolvableApiException){
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(DropsActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    // begin sending device location updates
    private void startLocationUpdates(){
        mFLPC.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
    }

    // builds menu recycler view
    private void buildMenu() {
        mMenuItems = new ArrayList<>();
        mMenuItems.add("Drops");
        mMenuItems.add("Friends");
        mMenuItems.add("Places");
        mMenuItems.add("Profile");

        mMenuRV = findViewById(R.id.drops_menu_recvw);
        mDropsTitleTV.setText("Drops");
        RecyclerView.LayoutManager menuLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MenuRecyclerViewAdapter menuAdapter = new MenuRecyclerViewAdapter(mMenuItems);
        mMenuRV.setLayoutManager(menuLayoutManager);
        mMenuRV.setAdapter(menuAdapter);
        menuAdapter.setOnItemClickListener(new MenuRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                doMenuAction(position);
            }
        });
    }

    // performs action of menu item clicked
    private void doMenuAction(int position) {
        mMenuRV.setEnabled(false);

        String label = mMenuItems.get(position);
        Toast.makeText(this, label, Toast.LENGTH_SHORT).show();

        if (label.equals("Drops")) {
        } else if (label.equals("Friends")) {
            openFriendsActivity();
        } else if (label.equals("Places")) {
            openLocationsActivity();
        } else if (label.equals("Profile")) {
            openProfileActivity();
        }
        mMenuRV.setEnabled(true);
    }


    // fills friends recycler view with items from friends list
    private void buildDropsRecyclerView() {
        mDropsLayoutManager = new LinearLayoutManager(this);
        mDropsAdapter = new DropsAdapter(mDrops);
        mDropsRV.setLayoutManager(mDropsLayoutManager);
        mDropsRV.setAdapter(mDropsAdapter);
        mDropsAdapter.setOnItemClickListener(new DropsAdapter.OnItemClickListener() {
            // shows a toast message with name of friend
            @Override
            public void onItemClick(View view, int position) {
                if(mDrops.get(position).getStatus() == 0){
                    Toast.makeText(DropsActivity.this, "Message: " + mDrops.get(position).getMessage(), Toast.LENGTH_SHORT).show();
                }
                else if(mDrops.get(position).getStatus() == 1){
                    Toast.makeText(DropsActivity.this, "Visit Lat: " + mDrops.get(position).getLatitude()
                            +"\n" + "Long: " + mDrops.get(position).getLongitude(), Toast.LENGTH_SHORT).show();
                }
                else if(mDrops.get(position).getStatus() == 2){
                    Toast.makeText(DropsActivity.this, "Message: " + mDrops.get(position).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            // calls removeDrop to delete selected drop
            @Override
            public void onDeleteClick(View view, int position) {
                removeDrop(position);
            }
        });
    }


    // listener for changes to user friends on database
    public void setDropsDatabaseListener() {
        Log.d(TAG, "setDropsDatabaseListener(): called");

        mRef.child(mUID + "/drops").orderByChild("status").addValueEventListener(new ValueEventListener() {
            // retrieves drops list from data base when change is detected
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: drops list changed");
                mDrops = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mDrops.add(snapshot.getValue(Messages.class));
                    Log.d(TAG, "Friend ID: " + snapshot.child("senderID").getValue());
                }
                buildDropsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void removeDrop(int position){
        mRef.child(mUID + "/drops/" + mDrops.get(position).getMessageID()).removeValue();
    }


    private void openFriendsActivity(){
        Intent intent = new Intent(DropsActivity.this, FriendsActivity.class);
        finish();
        startActivity(intent);
    }

    private void openLocationsActivity(){
        Intent intent = new Intent(DropsActivity.this, LocationsActivity.class);
        finish();
        startActivity(intent);
    }

    private void openProfileActivity(){
        Intent intent = new Intent(DropsActivity.this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}