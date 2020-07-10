package com.cbwise1997.udrop.Messages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbwise1997.udrop.Drops.DropsActivity;
import com.cbwise1997.udrop.Friends.FriendsActivity;
import com.cbwise1997.udrop.Locations.Loc;
import com.cbwise1997.udrop.Locations.LocationAdapter;
import com.cbwise1997.udrop.Locations.LocationsActivity;
import com.cbwise1997.udrop.MenuRecyclerViewAdapter;
import com.cbwise1997.udrop.ProfileActivity;
import com.cbwise1997.udrop.R;
import com.cbwise1997.udrop.UserInfo;
import com.google.android.gms.location.LocationCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessengerActivity extends AppCompatActivity {
    // Constants
    // IF ONE CONSTANT IS CHANGED IT MUST BE CHANGED IN EACH JAVA CLASS (trying to figure out a way to fix this)
    private String PROFILE_PREFS = "ProfilePrefs";
    private String GET_USER_ID_KEY= "UserIDKey";

    private Button mHomepageBtn ,mAddBtn, mSendLocation;
    private EditText mSubject, mMessage;
    private String TAG = "MessengerActivity", mUID;
    private static String locationName;
    private SharedPreferences mUserPrefs, senderID;
    private static double Latitude = 1, Longitude = 1;
    private static boolean isSelected = false;

    private static ArrayList<Loc> mLocations;
    private LocationAdapter mLocationAdapter;
    private static RecyclerView mLocationsRV;
    private RecyclerView.LayoutManager mLocationsLayoutManager;

    private ArrayList<String> mMenuItems;
    private RecyclerView mMenuRV;

    private static ArrayList<View> ViewArrays = new ArrayList<View>(1);
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private UserInfo mUserInfo;



    @Override
    protected void onCreate(Bundle InstanceState) {
        super.onCreate(InstanceState);
        setContentView(R.layout.activity_messenger);

        // Name all the buttons and connect them to the xml file
        mSubject = findViewById(R.id.messenger_subject_edttxt);
        mMessage = findViewById(R.id.messenger_message_edttxt);
        mHomepageBtn = findViewById(R.id.messenger_back_btn);
        mAddBtn = findViewById(R.id.messenger_add_btn);
        mSendLocation = findViewById(R.id.messenger_send_btn);

        mAuth = FirebaseAuth.getInstance();
        mUserPrefs = getSharedPreferences(PROFILE_PREFS, 0);
        mUID = mAuth.getUid();
        mRef = FirebaseDatabase.getInstance().getReference("users");

        getUserInfo();
        buildMenu();
        setLocationsDatabaseListener();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            Bundle mEmail = null;
            String emailToMessage = null;
            @Override
            public void onClick(View v) {
                //SEND THE MESSAGE HERE
                String subject = mSubject.getText().toString();
                String message = mMessage.getText().toString();
                if (subject.isEmpty() || message.isEmpty()) {
                    toaster("Email or password fields were left empty");
                    return;
                }
                else {
                    final Bundle userID = getIntent().getExtras();
                    if (userID != null) {
                        String userIDToMessage = userID.getString("friendUserID");
                        openMapsMessengerActivity(userIDToMessage, subject, message);
                    }
                }
            }
        });
        // On click listeners
        mHomepageBtn.setOnClickListener(new View.OnClickListener() {
            Bundle mEmail = null;
            String emailToMessage = null;
            @Override
            public void onClick(View v) {
                openFriendsActivity();
            }
        });



        mSendLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //SEND THE MESSAGE HERE
                String subject = mSubject.getText().toString();
                String message = mMessage.getText().toString();
                if (subject.isEmpty() || message.isEmpty()) {
                    toaster("Email or password fields were left empty");
                    return;
                }
                else if (isSelected == false) {
                    toaster("You did not select an address!!");
                }
                else {
                    final Bundle userID = getIntent().getExtras();
                    if (userID != null) {

                        String userIDToMessage = userID.getString("friendUserID");
                        Messages newMsg = new Messages(subject, message, mUserInfo, Latitude, Longitude, 1);
                        DatabaseReference ref = mRef.child(userIDToMessage + "/drops/").push();
                        String messageID = ref.getKey();
                        newMsg.setMessageID(messageID);
                        ref.setValue(newMsg);
                        toaster("Latitude: " + Latitude + " & Longitude: "+ Longitude);
                        Log.d(TAG,"Latitude: " + Latitude + " & Longitude: "+ Longitude);
                        openFriendsActivity();
                    }
                }
            }
        });
    }


    public void setLocationsDatabaseListener() {
        Log.d(TAG, "setLocationsDatabaseListener(): called");

        mRef.child(mUID + "/savedLocations").addValueEventListener(new ValueEventListener() {
            // retrieves friends list from data base when change is detected
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: location list changed");
                mLocations = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    double latitude = Double.parseDouble(snapshot.child("latitude").getValue().toString());
                    Log.d(TAG, "latitude="+latitude);
                    double longitude = Double.parseDouble(snapshot.child("longitude").getValue().toString());
                    Log.d(TAG, "longitude="+longitude);
                    String name = snapshot.child("name").getValue().toString();
                    Log.d(TAG, "name="+name);

                    mLocations.add(new Loc(name,latitude,longitude));
                    Log.d(TAG, "onDataChange: location object added");
                }
                buildLocationsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // onCreate functions ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void buildLocationsRecyclerView() {
        Log.d(TAG, "buildLocationsRecyclerView: called");
        mLocationsRV = findViewById(R.id.messenger_locations_rv);
        mLocationsLayoutManager = new LinearLayoutManager(this);
        mLocationAdapter = new LocationAdapter(mLocations);
        mLocationsRV.setLayoutManager(mLocationsLayoutManager);
        mLocationsRV.setAdapter(mLocationAdapter);
        mLocationAdapter.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!ViewArrays.isEmpty()) {

                    if (ViewArrays.get(0) == view){
                        view.setBackgroundColor(Color.WHITE);
                        ViewArrays.remove(0);
                        toaster("Latitude: " + Latitude + " & Longitude: "+ Longitude);
                        isSelected = false;
                    }
                    else {
                        ViewArrays.get(0).setBackgroundColor(Color.WHITE);
                        ViewArrays.set(0, view);
                        view.setBackgroundColor(Color.RED);
                        locationName = mLocations.get(position).getName();
                        Latitude = mLocations.get(position).getLatitude();
                        Longitude = mLocations.get(position).getLongitude();
                        toaster("Latitude: " + Latitude + " & Longitude: "+ Longitude);

                    }
                }

                else {
                    view.setBackgroundColor(Color.RED);
                    ViewArrays.add(view);
                    locationName = mLocations.get(position).getName();
                    Latitude = mLocations.get(position).getLatitude();
                    Longitude = mLocations.get(position).getLongitude();
                    toaster("Latitude: " + Latitude + " & Longitude: "+ Longitude);
                    isSelected = true;
                }

            }


            // calls removeFriend()
            @Override
            public void onDeleteClick(View view, int position) {
                //removeFriend(position);
            }
        });
        Log.d(TAG, "buildLocationsRecyclerView: completed");
    }


    // builds menu recycler view
    public void buildMenu() {
        mMenuItems = new ArrayList<>();
        mMenuItems.add("Drops");
        mMenuItems.add("Friends");
        mMenuItems.add("Places");
        mMenuItems.add("Profile");

        mMenuRV = findViewById(R.id.messenger_menu_rv);
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
    public void doMenuAction(int position) {
        mMenuRV.setEnabled(false);
        String label = mMenuItems.get(position);
        Toast.makeText(this, label, Toast.LENGTH_SHORT).show();
        if (label.equals("Drops")) {
            openDropsActivity();
        } else if (label.equals("Friends")) {
            openFriendsActivity();
        } else if (label.equals("Places")) {
            openLocationsActivity();
        } else if (label.equals("Profile")) {
            openProfileActivity();
        }
        mMenuRV.setEnabled(true);
    }

    // gets current user info from database
    private void getUserInfo() {
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

    private void toaster(String emailToMessage) { // Test to Make sure its the right username
        Toast.makeText(this, emailToMessage, Toast.LENGTH_SHORT).show();
    }

    public void openFriendsActivity(){
        Intent intent = new Intent(MessengerActivity.this, FriendsActivity.class);
        finish();
        startActivity(intent);
    }

    private void openMapsMessengerActivity(String userIDToMessage, String subject, String message) {
        Intent messengerActivity = new Intent(MessengerActivity.this, MapsMessengerActivity.class);
        messengerActivity.putExtra("friendUserID", userIDToMessage);
        messengerActivity.putExtra("friendSubject", subject);
        messengerActivity.putExtra("friendMessage", message);
        toaster(userIDToMessage + ' ' + subject + ' ' + message);
        finish();
        startActivity(messengerActivity);
    }

    private void openProfileActivity() {
        Intent dropsActivity = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(dropsActivity);
    }

    private void openDropsActivity() {
        Intent dropsActivity = new Intent(this, DropsActivity.class);
        finish();
        startActivity(dropsActivity);
    }

    private void openLocationsActivity() {
        Intent dropsActivity = new Intent(this, LocationsActivity.class);
        finish();
        startActivity(dropsActivity);
    }
}