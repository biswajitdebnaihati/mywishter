package com.exnovation.wishster.Models;

import com.google.gson.annotations.SerializedName;


public class UserModel {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("gender")
    public String gender;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("username")
    public String username;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("address")
    public String address;
    @SerializedName("profile_img")
    public String profile_img;
    @SerializedName("status")
    public int status;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public int getStatus() {
        return status;
    }
}
