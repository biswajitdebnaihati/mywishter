package com.exnovation.wishster.Models;

import com.google.gson.annotations.SerializedName;

public class SaveQuestion {
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public boolean status;

    public String getMessage()
    {
        return message;
    }

    public boolean isStatus()
    {
        return status;
    }
}
