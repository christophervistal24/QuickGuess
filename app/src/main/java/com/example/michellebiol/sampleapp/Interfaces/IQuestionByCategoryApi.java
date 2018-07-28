package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.CategoriesResponse;
import com.example.michellebiol.sampleapp.Models.QuestionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IQuestionByCategoryApi {
    @GET("api/auth/questionbycategories/{id}")
    Call<List<QuestionsResponse>> getQuestions(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );
}
