package com.cbwise1997.udrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cbwise1997.udrop.Drops.DropsActivity;
import com.cbwise1997.udrop.Friends.FriendsActivity;
import com.cbwise1997.udrop.Locations.LocationsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    // Constants
    // IF ONE CONSTANT IS CHANGED IT MUST BE CHANGED IN EACH JAVA CLASS (trying to figure out a way to fix this)
    private static final String TAG = "ProfileActivity";
    private String PROFILE_PREFS = "ProfilePrefs";


    private String USER_ID_KEY = "UserIDKey";
    private String FIRST_NAME_KEY = "FirstNameKey";
    private String LAST_NAME_KEY = "LastNameKey";
    private String NICKNAME_KEY = "NicknameKey";
    private String EMAIL_KEY = "EmailKey";
    private String PHONE_KEY = "PhoneKey";
    private static final int  GET_FROM_GALLERY = 3;

    // widgets
    private Button mSaveChangesBtn, mProfilePictureBtn;
    private EditText mNicknameET;
    private EditText mEmailET;
    private EditText mPhoneET;
    private RecyclerView mMenuRV;
    private TextView mViewTitleTV;
    private TextView mLogoutTV;
    private ImageView mImageIV;

    //vars

    private ArrayList<String> mMenuItems;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();
    // Create a child reference
    // imagesRef now points to "images"
    StorageReference imagesRef = storageRef.child("images");
    // instance for firebase storage and StorageReference

    StorageReference storageReference;

    // Uri indicates, where the image will be picked from
    private Uri filePath;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String mUID;
    private String IMAGE;
    private final int PICK_IMAGE_REQUEST = 22;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }


    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                mImageIV.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void init(){

        // Assigning values to member variables
        mSaveChangesBtn = findViewById(R.id.profile_save_changes_btn);
        mNicknameET = findViewById(R.id.profile_nickname_edttxt);
        mEmailET = findViewById(R.id.profile_email_edttxt);
        mPhoneET = findViewById(R.id.profile_phone_edttxt);
        mViewTitleTV = findViewById(R.id.profile_title_txtvw);
        mLogoutTV = findViewById(R.id.profile_logout_txtvw);
        mProfilePictureBtn = findViewById(R.id.profile_change_prof_pic_btn);
        mImageIV = findViewById(R.id.profile_prof_pic_imvw);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid());
        mUID = mAuth.getUid();
        IMAGE = mUID;

        storage = FirebaseStorage.getInstance();

        loadWithGlide();

        mProfilePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mSaveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        mLogoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        updateUI();
        buildMenu();
    }


    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageRef
                    .child(
                            "images/"
                                    + mUID);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(ProfileActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(ProfileActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    public void loadWithGlide() {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + mUID);

        // ImageView in your Activity
        ImageView imageView = findViewById(R.id.profile_prof_pic_imvw);

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
                .load(storageReference)
                .into(imageView);
        // [END storage_load_with_glide]
    }


    // Select Image method
    private void selectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    // builds menu recycler view
    public void buildMenu() {
        mMenuItems = new ArrayList<>();
        mMenuItems.add("Drops");
        mMenuItems.add("Friends");
        mMenuItems.add("Places");
        mMenuItems.add("Profile");

        mMenuRV = findViewById(R.id.profile_menu_recvw);
        mViewTitleTV.setText("Profile");

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
        }
        mMenuRV.setEnabled(true);
    }

    // fills profile UI with user info from shared prefs (not including profile picture)
    private void updateUI(){
        SharedPreferences prefs = getSharedPreferences(PROFILE_PREFS,0);

        mNicknameET.setText(prefs.getString(NICKNAME_KEY,""));
        mEmailET.setText(prefs.getString(EMAIL_KEY,""));
        mPhoneET.setText(prefs.getString(PHONE_KEY,""));
    }

    // will save changes to the nickname, email, and phone of user and update database accordingly
    private void saveChanges(){
        String nickname = mNicknameET.getText().toString();
        String email = mEmailET.getText().toString();
        String phone = mPhoneET.getText().toString();
        if (nickname.isEmpty() || email.isEmpty()) {
            toaster("No empty email or password!");
            return;
        }
        // Check if email id is valid or not
        else if (!isEmailValid(email)){
            toaster("Your Email Id is Invalid.");
            return;
        }
        else {
            toaster("Account has been updated.");
            saveUserInfo( nickname, phone, email);
        }
    }


    private void saveUserInfo(String nickname, String phone, String email){
        SharedPreferences prefs = getSharedPreferences(PROFILE_PREFS,0);

        String userID = mUID;
        prefs.edit().putString(NICKNAME_KEY, nickname).apply();
        prefs.edit().putString(EMAIL_KEY, email).apply();
        prefs.edit().putString(PHONE_KEY, phone).apply();

        //sends the new email to firebase authentication
        mAuth = FirebaseAuth.getInstance();
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User email address updated.");
                    // Toast.makeText(getActivity(), "The email updated.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //Saves image onto the database
        uploadImage();

        // sends the new user info to firebase database to be stored under users/(id)/userInfo
        mDatabaseReference.child("userInfo").child("nickname").setValue(nickname);
        mDatabaseReference.child("userInfo").child("email").setValue(email);
        mDatabaseReference.child("userInfo").child("phone").setValue(phone);
    }


    // opens friends activity
    private void openFriendsActivity(){
        Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
        finish();
        startActivity(intent);
    }

    // opens locations activity
    private void openLocationsActivity(){
        Intent intent = new Intent(ProfileActivity.this, LocationsActivity.class);
        finish();
        startActivity(intent);
    }

    // opens login activity
    private void openLoginActivity(){
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    // opens login activity
    private void openDropsActivity(){
        Intent intent = new Intent(ProfileActivity.this, DropsActivity.class);
        finish();
        startActivity(intent);
    }

    private void toaster(String emailToMessage) { // Test to Make sure its the right username
        Toast.makeText(this, emailToMessage, Toast.LENGTH_SHORT).show();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
