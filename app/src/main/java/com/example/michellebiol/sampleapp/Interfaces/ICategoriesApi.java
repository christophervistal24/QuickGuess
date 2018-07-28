package com.example.michellebiol.sampleapp.Interfaces;


import com.example.michellebiol.sampleapp.Models.CategoriesResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ICategoriesApi {
    @GET("api/auth/categories")
    Call<List<CategoriesResponse>> getCategory(@Header("Authorization") String authHeader);
}
