package com.exnovation.wishster.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignUpModel {
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public UserModel getUserData = null;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public UserModel getUserData() {
        return getUserData;
    }

    public static class ProfileData {
    @SerializedName("id")
    public int id;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("profile_img")
    public String profile_img;

    public int getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getProfile_img() {
        return profile_img;
    }
}
}
