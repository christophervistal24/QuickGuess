package com.example.michellebiol.sampleapp.PointModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Interfaces.IUserPointsApi;
import com.example.michellebiol.sampleapp.Models.Points.PointsRequest;
import com.example.michellebiol.sampleapp.Models.Points.PointsResponse;
import com.example.michellebiol.sampleapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Points {

    private int userPoints = 0;
    private Context context;

    public Points(Context context) {
        this.context = context;
    }
    public Points(int points)
    {
        this.setPoints(points);
    }

    public Points(){}

    public int getPoints() {
        return this.userPoints;
    }

    public void setPoints(int points) {
        this.userPoints = points * 100;
    }

    public void insertPoints(int points)
    {

        SharedPreferences sharedPref = context.getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token_type = sharedPref.getString("token_type","");
        String token = sharedPref.getString("token","");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IUserPointsApi services = retrofit.create(IUserPointsApi.class);
        PointsRequest pointsRequest = new PointsRequest();

        pointsRequest.setPoints(points);
        Call<PointsResponse> pointsResponseCall = services.userPoints(token_type.concat(token),pointsRequest);
        pointsResponseCall.enqueue(new Callback<PointsResponse>() {
            @Override
            public void onResponse(Call<PointsResponse> call, Response<PointsResponse> response) {
                if  (response.isSuccessful())
                {
                    PointsResponse pointsResponse = response.body();
                }
            }

            @Override
            public void onFailure(Call<PointsResponse> call, Throwable t) {

            }
        });

    }
}
