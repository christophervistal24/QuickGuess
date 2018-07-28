package com.example.michellebiol.sampleapp.Models;

import com.google.gson.annotations.SerializedName;

public class AccountsResponse {
    private String user_id;

    @SerializedName("user")
    private User user;



    public AccountsResponse(String user_id , User user) {
        this.user_id = user_id;
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public User getUser() {
        return user;
    }
}


