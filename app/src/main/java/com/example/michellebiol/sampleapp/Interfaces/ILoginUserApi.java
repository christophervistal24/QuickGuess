package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.TokenRequest;
import com.example.michellebiol.sampleapp.Models.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ILoginUserApi {
    @POST("api/auth/login")
    Call<TokenResponse> loginRequest(@Body TokenRequest tokenRequest);
}
