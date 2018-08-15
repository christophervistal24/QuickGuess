package com.example.michellebiol.sampleapp.RegisterModule;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.HomeActivity;
import com.example.michellebiol.sampleapp.Interfaces.IRegisterUserApi;
import com.example.michellebiol.sampleapp.Models.RegisterUserRequest;
import com.example.michellebiol.sampleapp.Models.RegisterUserResponse;
import com.example.michellebiol.sampleapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class RegisterUser {
    IRegisterUserApi services;
    private Context context;
    private String id;

    public RegisterUser(Context context)
    {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit.create(IRegisterUserApi.class);
    }

    public void setCredentials(String[] phone_info , String[] user_credentials)
    {
        RegisterUserRequest registerUser = new RegisterUserRequest();
        registerUser.setUsername(user_credentials[0]);
        registerUser.setPassword(user_credentials[1]);
        registerUser.setQuestion(user_credentials[2]);
        registerUser.setAnswer(user_credentials[3]);
        registerUser.setAndroid_id(phone_info[0]);
        registerUser.setAndroid_mac(phone_info[1]);
        registerNewUser(registerUser);

    }

    public void registerNewUser(RegisterUserRequest registerUser)
    {

        Call<RegisterUserResponse> registerUserResponseCall = services.newUser(registerUser);
        registerUserResponseCall.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {

                if (response.isSuccessful()) {
                    RegisterUserResponse registerUserResponse = response.body();
                    String responseMessage = registerUserResponse != null ? registerUserResponse.getMessage() : null;
                    String statusMessage = getRegisterMessage(responseMessage,registerUserResponse);
                    Toast.makeText(context, "Message : " + statusMessage, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
            }
        });

    }

    private String getRegisterMessage(String responseMessage , RegisterUserResponse registerResponse)
    {
        if(responseMessage != null && responseMessage.equals("Success"))
        {
            SharedPreferences settings = context.getSharedPreferences(registerResponse.getId().concat("_life"), 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("life",5);
            editor.apply();
            HomeActivity.activityInstance().setToken(initializeTokens(registerResponse));
        } else {
            HomeActivity.activityInstance().openInputDialog();
        }
        return responseMessage;
    }

    private String[] initializeTokens(RegisterUserResponse registerResponse)
    {
        String token = registerResponse.getAccess_token();
        String token_type = registerResponse.getToken_type();
        String user_id = registerResponse.getId();
        return new String[]{token, token_type, user_id};
    }


}
