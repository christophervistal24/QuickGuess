package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.LoginAnswer;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ILoginAnswerApi {
    @POST("api/auth/user/answer/{user_id}/{user_answer}")
    Call<LoginAnswer> getLoginAnswerResult(
            @Path("user_id") int user_id,
            @Path("user_answer") String user_anwer
    );
}
