package com.exnovation.wishster.Models;

import com.google.gson.annotations.SerializedName;

public class Questions {
    @SerializedName("id")
    public int id;
    @SerializedName("question")
    public String question;
    @SerializedName("option1")
    public String option1;
    @SerializedName("option2")
    public String option2;
    @SerializedName("option3")
    public String option3;
    @SerializedName("option4")
    public String option4;

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }
}
