package com.example.kartelapp;

public class UserActivity {
    private final String userID;
    private final String userName;
    private final String profileUrl;

    public UserActivity(String userID, String userName, String profileUrl) {
        this.userID = userID;
        this.userName = userName;
        this.profileUrl = profileUrl;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

}
