package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.RegisterUserRequest;
import com.example.michellebiol.sampleapp.Models.RegisterUserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IRegisterUserApi {
    @POST("api/auth/register")
    Call<RegisterUserResponse> newUser(@Body RegisterUserRequest registerUser);
}
