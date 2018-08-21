package com.example.michellebiol.sampleapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsernameResponse {
    @SerializedName("found")
    private String found;

    @SerializedName("user_id")
    private int id;

    public String getFound() {
        return found;
    }

    public int getId() {return id;}
}
