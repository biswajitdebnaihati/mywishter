package com.exnovation.wishster.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewQuestion {

        @SerializedName("message")
        public String message;
        @SerializedName("status")
        public boolean status;
        @SerializedName("data")
        public List<Questions> getQuestions = null;

        public String getMessage() {
                return message;
        }

        public boolean isStatus() {
                return status;
        }

        public List<Questions> getGetQuestions() {
                return getQuestions;
        }
}
