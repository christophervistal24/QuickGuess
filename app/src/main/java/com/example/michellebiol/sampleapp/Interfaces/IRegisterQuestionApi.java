package com.example.michellebiol.sampleapp.Interfaces;


import com.example.michellebiol.sampleapp.Models.RegisterQuestionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IRegisterQuestionApi {
    @GET("api/auth/registerquestions")
   Call<List<RegisterQuestionResponse>> getQuestions();
}
