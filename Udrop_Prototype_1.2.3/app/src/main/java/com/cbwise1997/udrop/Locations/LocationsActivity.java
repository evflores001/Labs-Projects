package com.cbwise1997.udrop.Locations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbwise1997.udrop.Drops.DropsActivity;
import com.cbwise1997.udrop.Friends.FriendsActivity;
import com.cbwise1997.udrop.MapsActivity;
import com.cbwise1997.udrop.MenuRecyclerViewAdapter;
import com.cbwise1997.udrop.ProfileActivity;
import com.cbwise1997.udrop.R;
import com.cbwise1997.udrop.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationsActivity extends AppCompatActivity {


    // Constants
    // IF ONE CONSTANT IS CHANGED IT MUST BE CHANGED IN EACH JAVA CLASS (trying to figure out a way to fix this)
    private String PROFILE_PREFS = "ProfilePrefs";
    private String TAG = "LocationActivity";

    private String USER_ID_KEY = "UserIDKey";
    private String FIRST_NAME_KEY = "FirstNameKey";
    private String LAST_NAME_KEY = "LastNameKey";
    private String NICKNAME_KEY = "NicknameKey";
    private String EMAIL_KEY = "EmailKey";
    private String PHONE_KEY = "PhoneKey";

    // Member Variables

    private TextView mViewTitleTV;

    private Button mNewLocationBtn;
    private EditText mNewLocationET;
    private ArrayList<Loc> mLocations;
    private RecyclerView mLocationsRV;
    private LocationAdapter mLocationAdapter;
    private RecyclerView.LayoutManager mLocationsLayoutManager;

    private ArrayList<String> mMenuItems;
    private RecyclerView mMenuRV;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private boolean mIsSaved;

    private SharedPreferences mUserPrefs;
    private UserInfo mUserInfo;
    private String mUID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        mViewTitleTV = findViewById(R.id.locations_title_tv);
        mNewLocationET = findViewById(R.id.locations_new_location_et);
        mNewLocationBtn = findViewById(R.id.locations_new_location_btn);

        mAuth = FirebaseAuth.getInstance();
        mUserPrefs = getSharedPreferences(PROFILE_PREFS, 0);
        mRef = FirebaseDatabase.getInstance().getReference("users");
        mUID = mAuth.getUid();

        buildMenu();
        init();
        setLocationsDatabaseListener();
    }


    public void setLocationsDatabaseListener() {
        Log.d(TAG, "setLocationsDatabaseListener(): called");

        mRef.child(mUID + "/savedLocations").addValueEventListener(new ValueEventListener() {
            // retrieves locations list from data base when change is detected
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: location list changed");
                mLocations = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mLocations.add(snapshot.getValue(Loc.class));
                    Log.d(TAG, "onDataChange: location object added");
                }
                buildLocationsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //deletes saved location at specified position
    public void removeLocation(int position) {
        Log.d(TAG, "removeLocation: called");
        mRef.child(mUID + "/savedLocations/" + mLocations.get(position).getName()).removeValue();
    }


    public void init(){
        mNewLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mNewLocationET.getText().toString().equals("")){
                    // opens maps activity and passes string from edit text as extra
                    openMapsActivity();
                }
            }
        });
    }


    // builds menu recycler view
    public void buildMenu() {
        mMenuItems = new ArrayList<>();
        mMenuItems.add("Drops");
        mMenuItems.add("Friends");
        mMenuItems.add("Places");
        mMenuItems.add("Profile");

        mMenuRV = findViewById(R.id.locations_menu_rv);
        mViewTitleTV.setText("Places");

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


    public void buildLocationsRecyclerView() {
        Log.d(TAG, "buildLocationsRecyclerView: called");
        mLocationsRV = findViewById(R.id.locations_locations_rv);
        mLocationsLayoutManager = new LinearLayoutManager(this);
        mLocationAdapter = new LocationAdapter(mLocations);
        mLocationsRV.setLayoutManager(mLocationsLayoutManager);
        mLocationsRV.setAdapter(mLocationAdapter);
        mLocationAdapter.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
            // shows a toast message with name of friend
            @Override
            public void onItemClick(View view, int position) {
                //openLocationsActivity();
            }


            // calls removeLocation()
            @Override
            public void onDeleteClick(View view, int position) {
                Log.d(TAG, "onDeleteClick: called");
                removeLocation(position);
            }
        });
        Log.d(TAG, "buildLocationsRecyclerView: completed");
    }


    // performs action of menu item clicked
    public void doMenuAction(int position) {
        mMenuRV.setEnabled(false);
        String label = mMenuItems.get(position);
        Toast.makeText(this, label, Toast.LENGTH_SHORT).show();

        if (label.equals("Drops")) {
            openDropsActivity();
        } else if (label.equals("Friends")) {
            openFriendsActivity();
        } else if (label.equals("Profile")) {
            openProfileActivity();
        }
        mMenuRV.setEnabled(true);
    }


    public void openMapsActivity(){
        Intent intent = new Intent(LocationsActivity.this, MapsActivity.class);
        intent.putExtra("NEW_LOCATION", mNewLocationET.getText().toString());
        finish();
        startActivity(intent);
    }

    public void openFriendsActivity(){
        Intent intent = new Intent(LocationsActivity.this, FriendsActivity.class);
        finish();
        startActivity(intent);
    }

    public void openDropsActivity(){
        Intent intent = new Intent(LocationsActivity.this, DropsActivity.class);
        finish();
        startActivity(intent);
    }

    public void openProfileActivity(){
        Intent intent = new Intent(LocationsActivity.this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}