package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.GetUserRegisterQuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IGetUserRegisterQuestionApi {
    @GET("api/auth/user/register/question/{id}")
    Call<GetUserRegisterQuestionResponse> getRegisterQuestion(@Path("id") int user_id);
}
