package com.example.michellebiol.sampleapp.Interfaces;

import com.example.michellebiol.sampleapp.Models.Points.PointsRequest;
import com.example.michellebiol.sampleapp.Models.Points.PointsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IUserPointsApi {
    @POST("api/auth/userrank")
    Call<PointsResponse> userPoints(@Header("Authorization") String AuthHeader ,
                                    @Body PointsRequest pointsRequest);
}
