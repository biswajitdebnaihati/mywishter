package com.exnovation.wishster.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginModel {
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public UserModel getUserDetails = null;

    public UserModel getUserDetails() {
        return getUserDetails;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
