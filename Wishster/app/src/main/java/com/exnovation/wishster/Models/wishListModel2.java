package com.exnovation.wishster.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class wishListModel2 {
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public List<WishListSubModel> data = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public List<WishListSubModel> getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }
}
