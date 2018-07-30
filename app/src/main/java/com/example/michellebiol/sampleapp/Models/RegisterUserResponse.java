package com.example.michellebiol.sampleapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUserResponse {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("access_token")
    @Expose
    private String access_token;

    @SerializedName("token_type")
    @Expose
    private String token_type;

    @SerializedName("id")
    @Expose
    private String id;




    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getId() { return id; }
}
