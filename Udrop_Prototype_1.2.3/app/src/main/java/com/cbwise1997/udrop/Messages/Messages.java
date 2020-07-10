package com.cbwise1997.udrop.Messages;

import android.location.Location;

import com.cbwise1997.udrop.UserInfo;

public class Messages {
    private Location location;
    private String subject;
    private String message;
    private UserInfo senderInfo;
    private String messageID;
    private double latitude;
    private double longitude;
    private int status;

    public Messages() {
    }

    public Messages(String subject, String message, UserInfo senderInfo, double latitude, double longitude, int status) {
        this.subject = subject;
        this.message = message;
        this.senderInfo = senderInfo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }


    public String getSubject() { return subject; }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getMessage() { return message; }

    public UserInfo getSenderInfo() { return senderInfo; }

    public String getMessageID() {
        return messageID;
    }

    public int getStatus() {
        return status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderID(UserInfo senderInfo) {
        this.senderInfo = senderInfo;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
