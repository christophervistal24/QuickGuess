package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.AccountsResponse;
import com.example.michellebiol.sampleapp.Models.CategoriesResponse;
import com.example.michellebiol.sampleapp.Models.QuestionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IAccountsApi {
    @GET("api/auth/accounts/{android_id}")
    Call<List<AccountsResponse>> getAccounts(
            @Header("Authorization") String authHeader,
            @Path("android_id") String id
    );
}
