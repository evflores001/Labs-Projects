package com.cbwise1997.udrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.cbwise1997.udrop.Friends.FriendsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // Constants
    // IF ONE CONSTANT IS CHANGED THEY MUST ALL BE CHANGED IN EACH JAVA CLASS
    private String PROFILE_PREFS = "ProfilePrefs";

    private String USER_ID_KEY = "UserIDKey";
    private String FIRST_NAME_KEY = "FirstNameKey";
    private String LAST_NAME_KEY = "LastNameKey";
    private String NICKNAME_KEY = "NicknameKey";
    private String EMAIL_KEY = "EmailKey";
    private String PHONE_KEY = "PhoneKey";

    //Member Variables
    private Button mSignInBtn;
    private Button mSignUpBtn;
    private EditText mEmailET;
    private EditText mPasswordET;

    private Switch mSaveLoginCheckBox;
    private SharedPreferences mLoginPreferences;
    private SharedPreferences.Editor mLoginPrefsEditor;
    private Boolean mSaveLoginBool;

    private FirebaseAuth mAuth;
    private DatabaseReference mUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Assigning member variable values

        mSignInBtn = findViewById(R.id.loginSignIn_Btn);
        mSignUpBtn = findViewById(R.id.loginSignUp_Btn);
        mEmailET = findViewById(R.id.loginEmail_ET);
        mPasswordET = findViewById(R.id.loginPassword_ET);

        mSaveLoginCheckBox = (Switch)findViewById(R.id.saveLoginCheckBox);
        mLoginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        mLoginPrefsEditor = mLoginPreferences.edit();
        mSaveLoginBool = mLoginPreferences.getBoolean("saveLogin", false);

        mAuth = FirebaseAuth.getInstance();
        mUsersRef = FirebaseDatabase.getInstance().getReference("users");


        // if save login info is checked, then autofill login fields with user login info
        if (mSaveLoginBool) {
            mEmailET.setText(mLoginPreferences.getString("username", ""));
            mPasswordET.setText(mLoginPreferences.getString("password", ""));
            mSaveLoginCheckBox.setChecked(true);
        }

        // On click listeners
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignIn();
            }
        });
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
    }

    // attempt to sign in with email and password input by user
    private void attemptSignIn(){
        String email = mEmailET.getText().toString();
        String password = mPasswordET.getText().toString();

        // user left email or password fields empty
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Email or password fields were left empty", Toast.LENGTH_SHORT).show();
            return;
        }

        //Part 2 of Saving information -Ali
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailET.getWindowToken(), 0);

        // if save login info is checked, then save user login info in loginPrefs
        if (mSaveLoginCheckBox.isChecked()) {
            mLoginPrefsEditor.putBoolean("saveLogin", true);
            mLoginPrefsEditor.putString("username", email);
            mLoginPrefsEditor.putString("password", password);
            mLoginPrefsEditor.commit();
        }
        else {
            mLoginPrefsEditor.clear();
            mLoginPrefsEditor.commit();
        }

        Toast.makeText(this,"Logging in...", Toast.LENGTH_SHORT).show();

        // checking email and password against database email and password
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Udrop", "signInWithEmailAndPassword() onComplete" + task.isSuccessful());
                // unsuccessful login
                if(!task.isSuccessful()){
                    Log.d("Udrop", "Problem signing in: " + task.getException());
                    showErrorDialog("There was a problem signing in...");
                }
                // successful login
                else {
                    getUserInfo();
                    openHomepageActivity();
                }
            }
        });
    }

    // get user info from firebase database and store in PROFILE_PREFS
    private void getUserInfo(){
        DatabaseReference userRef = mUsersRef.child(mAuth.getUid()).child("userInfo");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userID = dataSnapshot.child("userID").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String nickname = dataSnapshot.child("nickname").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);

                SharedPreferences prefs = getSharedPreferences(PROFILE_PREFS,0);

                prefs.edit().putString(USER_ID_KEY, userID).apply();
                prefs.edit().putString(FIRST_NAME_KEY, firstName).apply();
                prefs.edit().putString(LAST_NAME_KEY, lastName).apply();
                prefs.edit().putString(NICKNAME_KEY, nickname).apply();
                prefs.edit().putString(EMAIL_KEY, email).apply();
                prefs.edit().putString(PHONE_KEY, phone).apply();

                Log.d("Udrop","User ID: " + userID);
                Log.d("Udrop","User name: " + firstName + " " + lastName);
                Log.d("Udrop","User nickname: " + nickname);
                Log.d("Udrop","User email: " + email);
                Log.d("Udrop","User phone: " + phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // open HomepageActivity
    private void openHomepageActivity(){
        Intent intent = new Intent(LoginActivity.this, FriendsActivity.class);
        finish();
        startActivity(intent);
    }

    // open SignUpActivity
    private void openSignUpActivity(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        finish();
        startActivity(intent);
    }

    // alert dialog for when something goes wrong
    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton("ok", null)
                .show();
    }
}