package com.cbwise1997.udrop.Friends;

public class FriendRequestItem {
    private int mProfPic;
    private int mAccept;
    private int mDecline;
    private String mName;

    public FriendRequestItem(int profPic, int accept, int decline, String name){
        mProfPic = profPic;
        mAccept = accept;
        mDecline = decline;
        mName = name;
    }

    public String getName(){
        return mName;
    }

    public int getProfPic(){
        return mProfPic;
    }

    public int getAccept() {
        return mAccept;
    }

    public int getDecline() {
        return mDecline;
    }

    public void setName(String name){
        mName = name;
    }

    public void setProfPic(int profPic){
        mProfPic = profPic;
    }

    public void setAccept(int accept) {
        mAccept = accept;
    }

    public void setDecline(int decline) {
        mDecline = decline;
    }
}