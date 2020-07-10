package com.cbwise1997.udrop.Friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cbwise1997.udrop.Drops.DropsActivity;
import com.cbwise1997.udrop.Locations.LocationsActivity;
import com.cbwise1997.udrop.LoginActivity;
import com.cbwise1997.udrop.MenuRecyclerViewAdapter;
import com.cbwise1997.udrop.Messages.MessengerActivity;
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

public class FriendsActivity extends AppCompatActivity {

    // Constants
    // IF ONE CONSTANT IS CHANGED IT MUST BE CHANGED IN EACH JAVA CLASS (trying to figure out a way to fix this)
    private String PROFILE_PREFS = "ProfilePrefs";
    private String TAG = "FriendsActivity";

    private String USER_ID_KEY = "UserIDKey";
    private String FIRST_NAME_KEY = "FirstNameKey";
    private String LAST_NAME_KEY = "LastNameKey";
    private String NICKNAME_KEY = "NicknameKey";
    private String EMAIL_KEY = "EmailKey";
    private String PHONE_KEY = "PhoneKey";

    // Member Variables

    private TextView mViewTitleTV;

    private ImageButton mAddFriendImgBtn;
    private EditText mAddFriendET;
    private ArrayList<UserInfo> mFriendRequests;
    private ArrayList<UserInfo> mFriends;
    private RecyclerView mFriendsRV;
    private FriendListAdapter mFriendListAdapter;
    private RecyclerView.LayoutManager mFriendLayoutManager;


    private ArrayList<String> mMenuItems;
    private RecyclerView mMenuRV;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private SharedPreferences mUserPrefs;
    private UserInfo mUserInfo;
    private String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        init();
    }
// onCreate functions ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void init(){
        mViewTitleTV = findViewById(R.id.friends_title_txtvw);
        mAddFriendImgBtn = findViewById(R.id.friends_add_friend_imgbtn);
        mAddFriendET = findViewById(R.id.friends_add_friend_edttxt);

        mFriendRequests = new ArrayList<>();
        mFriends = new ArrayList<>();
        mFriendListAdapter = new FriendListAdapter(mFriends, mFriendRequests);

        mAuth = FirebaseAuth.getInstance();

        mUserPrefs = getSharedPreferences(PROFILE_PREFS, 0);
        mRef = FirebaseDatabase.getInstance().getReference("users");
        mUID = mAuth.getUid();


        //Listeners

        mAddFriendImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toaster("Sent friend request to\n" + mAddFriendET.getText());
                sendFriendRequest();
            }
        });

        setFriendRequestDatabaseListener();
        setFriendsDatabaseListener();

        // function calls

        getUserInfo();
        buildMenu();
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

    //

// MENU FUNCTIONS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // builds menu recycler view
    public void buildMenu() {
        mMenuItems = new ArrayList<>();
        mMenuItems.add("Drops");
        mMenuItems.add("Friends");
        mMenuItems.add("Places");
        mMenuItems.add("Profile");

        mMenuRV = findViewById(R.id.friends_menu_recvw);
        mViewTitleTV.setText("Friends");
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
            buildFriendsRecyclerView();
        } else if (label.equals("Places")) {
            mMenuRV.setEnabled(true);
            openLocationsActivity();
        } else if (label.equals("Profile")) {
            mMenuRV.setEnabled(true);
            openProfileActivity();
        }
        mMenuRV.setEnabled(true);
    }



// FRIENDS LIST FUNCTIONS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // listener for changes to user friends on database
    public void setFriendsDatabaseListener() {
        Log.d(TAG, "setFriendsDatabaseListener(): called");

        mRef.child(mUID + "/friendsList").orderByChild("firstName").addValueEventListener(new ValueEventListener() {
            // retrieves friends list from data base when change is detected
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: friends list changed");
                mFriends = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mFriends.add(snapshot.getValue(UserInfo.class));
                    Log.d(TAG, "Friend email: " + snapshot.child("email").getValue());
                }
                buildFriendsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // listener for changes to user friend requests on database
    public void setFriendRequestDatabaseListener() {
        Log.d(TAG, "setFriendRequestDatabaseListener(): called");

        mRef.child(mUID + "/friendRequests").orderByChild("firstName").addValueEventListener(new ValueEventListener() {
            // retrieves friends list from data base when change is detected
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: friend requests list changed");
                mFriendRequests = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Sender email: " + snapshot.child("email").getValue());
                    mFriendRequests.add(snapshot.getValue(UserInfo.class));
                }
                buildFriendsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // create a new friend request from sender in receivers friend requests on database
    public void sendFriendRequest() {
        mAddFriendImgBtn.setEnabled(false);
        final String receiverEmail = mAddFriendET.getText().toString().trim();
        mAddFriendET.setText("");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    // checking if snapshot is correct receiver
                    if (snapshot.child("userInfo/email").getValue().toString().equals(receiverEmail)) {

                        // checking if receiver is already friends with sender
                        if(!snapshot.child("friendsList/" + mUID).exists()){
                            mRef.child(snapshot.getKey() + "/friendRequests/" + mUID).setValue(mUserInfo);
                        }

                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mAddFriendImgBtn.setEnabled(true);
    }

    // not currently used in app (used only for testing)
    // adds friend to friends list without the need for sending or accepting friend request
    public void addFriend(final String email) {
        mAddFriendImgBtn.setEnabled(false);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("userInfo/email").getValue().equals(email)) {
                        Log.d(TAG, "Add Friend: " + snapshot.getValue());
                        UserInfo friendInfo = snapshot.child("userInfo").getValue(UserInfo.class);
                        Log.d(TAG, "FriendUID: " + friendInfo.getUserID());
                        // adding friend to user friendList in firebase
                        mRef.child(mUID + "/friendsList/" + friendInfo.getUserID())
                                .setValue(friendInfo);

                        // adding user to friend friendList in firebase
                        mRef.child(friendInfo.getUserID() + "/friendsList/" + mUID)
                                .setValue(mUserInfo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mAddFriendImgBtn.setEnabled(true);
    }


    // removes friend from database, prompting recycler view to update
    public void removeFriend(int position) {
        String friendID = mFriends.get(position).getUserID();
        mRef.child(mUID + "/friendsList/" + friendID).removeValue();
        mRef.child(friendID + "/friendsList/" + mUID).removeValue();

        //leave below lines commented for now, causes weird animation and potential app crash
        //mFriends.remove(position);
        //mFriendAdapter.notifyItemRemoved(position);
    }

    // removes friend request from database, prompting recycler view to update
    public void declineFriendRequest(int position) {
        Log.d(TAG, "position: " + position);
        Log.d(TAG, "size: " + mFriendRequests.size());

        String friendID = mFriendRequests.get(position).getUserID();
        mRef.child(mUID + "/friendRequests/" + friendID).removeValue();
    }

    // adds friend to database, removes friend request from database, prompting both recycler views to update
    public void acceptFriendRequest(int position) {
        String friendID = mFriendRequests.get(position).getUserID();

        mRef.child(mUID + "/friendsList/" + friendID).setValue(mFriendRequests.get(position));
        mRef.child(friendID + "/friendsList/" + mUID).setValue(mUserInfo);

        mRef.child(mUID + "/friendRequests/" + friendID).removeValue();
        mRef.child(friendID + "/friendRequests/" + mUID).removeValue();
    }

    // fills friends recycler view with items from friends list
    public void buildFriendsRecyclerView() {
        mFriendsRV = findViewById(R.id.friends_friends_recvw);
        mFriendLayoutManager = new LinearLayoutManager(this);
        mFriendListAdapter = new FriendListAdapter(mFriends, mFriendRequests);
        mFriendsRV.setLayoutManager(mFriendLayoutManager);
        mFriendsRV.setAdapter(mFriendListAdapter);
        mFriendListAdapter.setOnItemClickListener(new FriendListAdapter.OnItemClickListener() {
            // shows a toast message with name of friend
            @Override
            public void onItemClick(View view, int position) {
                if(position >= mFriendRequests.size()){
                    String userIDToMessage = mFriends.get(position - mFriendRequests.size()).getUserID();
                    openMessengerActivity(userIDToMessage);
                }
            }

            @Override
            public void onAcceptClick(View v, int position) {
                if(position < mFriendRequests.size()){
                    acceptFriendRequest(position);
                }
            }

            @Override
            public void onDeclineClick(View v, int position) {
                if(position < mFriendRequests.size()){
                    declineFriendRequest(position);
                }
            }

            // calls removeFriend()
            @Override
            public void onDeleteClick(View view, int position) {
                if(position >= mFriendRequests.size()){
                    acceptFriendRequest(position - mFriendRequests.size());
                }
            }
        });
    }



    // had to use this in the many functions to make a Toast message because it wouldn't
    // otherwise. kinda weird
    private void toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

// open new activity functions //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void openProfileActivity() {
        Intent profileActivity = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(profileActivity);
    }

    private void openFriendsActivity() {
        Intent friendsActivity = new Intent(this, FriendsActivity.class);
        finish();
        startActivity(friendsActivity);
    }

    private void openDropsActivity() {
        Intent dropsActivity = new Intent(this, DropsActivity.class);
        finish();
        startActivity(dropsActivity);
    }

    private void openLocationsActivity() {
        Intent locationsActivity = new Intent(this, LocationsActivity.class);
        finish();
        startActivity(locationsActivity);
    }

    private void openLoginActivity() {
        Intent dropsActivity = new Intent(this, LoginActivity.class);
        finish();
        startActivity(dropsActivity);
    }

    private void openMessengerActivity(String userIDToMessage) {

        Intent messengerActivity = new Intent(this, MessengerActivity.class);
        messengerActivity.putExtra("friendUserID", userIDToMessage);
        toaster("going to next" + messengerActivity);
        finish();
        startActivity(messengerActivity);
    }
}