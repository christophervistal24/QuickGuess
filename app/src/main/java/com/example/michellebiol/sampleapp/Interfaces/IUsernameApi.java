package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.QuestionsResponse;
import com.example.michellebiol.sampleapp.Models.UsernameResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IUsernameApi {
        @GET("api/auth/account/username/{username}")
        Call<UsernameResponse> getCheckResult(@Path("username") String username);
}
