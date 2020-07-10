package com.cbwise1997.udrop;

public class UserInfo {

    // Attributes

    private String userID;
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String phone;

    // Constructors

    UserInfo(String userID, String firstName, String lastName, String nickname, String email, String phone){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
    }

    public UserInfo() {
    }

    // Accessor Functions

    public String getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}