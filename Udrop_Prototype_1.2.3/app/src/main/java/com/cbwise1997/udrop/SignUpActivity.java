package com.cbwise1997.udrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cbwise1997.udrop.Friends.FriendsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    // Constants
    // IF ONE CONSTANT IS CHANGED IT MUST BE CHANGED IN EACH JAVA CLASS (trying to figure out a way to fix this)
    private String PROFILE_PREFS = "ProfilePrefs";

    private String USER_ID_KEY = "UserIDKey";
    private String FIRST_NAME_KEY = "FirstNameKey";
    private String LAST_NAME_KEY = "LastNameKey";
    private String NICKNAME_KEY = "NicknameKey";
    private String EMAIL_KEY = "EmailKey";
    private String PHONE_KEY = "PhoneKey";

    // Member Variables
    private Button mSignInBtn;
    private Button mSignUpBtn;
    private EditText mFirstNameET;
    private EditText mLastNameET;
    private EditText mNicknameET;
    private EditText mEmailET;
    private EditText mPhoneET;
    private EditText mPasswordET;
    private EditText mConfirmPasswordET;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Assigning values to member variables

        mSignInBtn = findViewById(R.id.signUpSignInActivity_Btn);
        mSignUpBtn = findViewById(R.id.signUpSignUp_BTN);

        mFirstNameET = findViewById(R.id.signUpFirstName_ET);
        mLastNameET = findViewById(R.id.signUpLastName_ET);
        mNicknameET = findViewById(R.id.signUpNickname_ET);
        mEmailET = findViewById(R.id.signUpEmail_ET);
        mPhoneET = findViewById(R.id.signUpPhone_ET);
        mPasswordET = findViewById(R.id.signUpPassword_ET);
        mConfirmPasswordET = findViewById(R.id.signUpConfirmPassword_ET);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        // On click listeners

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });

    }

    // attempt to register new user on Firebase Database using email and password
    private void attemptRegistration(){

        // information entered by user
        final String firstName = mFirstNameET.getText().toString().trim();
        final String lastName = mLastNameET.getText().toString().trim();
        final String nickname = mNicknameET.getText().toString().trim();
        final String email = mEmailET.getText().toString().trim();
        final String phone = mPhoneET.getText().toString().trim();
        final String password = mPasswordET.getText().toString().trim();
        final String confirmPassword = mConfirmPasswordET.getText().toString().trim();

        // checking if user input values are valid
        if(!firstName.isEmpty() && !lastName.isEmpty()
                && !email.isEmpty() && email.contains("@")
                && password.length() > 3 && password.equals(confirmPassword)) {

            // attempting to create a new user on Firebase Database
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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

                        saveUserInfo(mAuth.getUid(), firstName, lastName, nickname, email, phone);
                        openHomepageActivity();
                    }
                }
            });
        }
        else {
            Toast.makeText(this,"One or more sign up fields are invalid...",Toast.LENGTH_SHORT).show();
        }
    }

    // save user info to database and shared prefs
    private void saveUserInfo(String userID ,String firstName, String lastName, String nickname, String email, String phone){
        SharedPreferences prefs = getSharedPreferences(PROFILE_PREFS,0);

        prefs.edit().putString(USER_ID_KEY, userID).apply();
        prefs.edit().putString(FIRST_NAME_KEY, firstName).apply();
        prefs.edit().putString(LAST_NAME_KEY, lastName).apply();
        prefs.edit().putString(NICKNAME_KEY, nickname).apply();
        prefs.edit().putString(EMAIL_KEY, email).apply();
        prefs.edit().putString(PHONE_KEY, phone).apply();

        UserInfo newUser = new UserInfo(userID,firstName,lastName,nickname,email,phone);

        // sends the new user info to firebase database to be stored under users/(id)/userInfo
        mDatabaseReference.child(userID).child("userInfo").setValue(newUser);
    }

    // opens login activity
    private void openLoginActivity(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    // opens homepage activity
    private void openHomepageActivity(){
        Intent intent = new Intent(SignUpActivity.this, FriendsActivity.class);
        finish();
        startActivity(intent);
    }

    // alert dialog for when something went wrong
    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton("ok", null)
                .show();
    }
}