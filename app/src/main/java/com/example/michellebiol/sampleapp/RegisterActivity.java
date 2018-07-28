package com.example.michellebiol.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Interfaces.IRegisterUserApi;
import com.example.michellebiol.sampleapp.Models.RegisterUserRequest;
import com.example.michellebiol.sampleapp.Models.RegisterUserResponse;
import com.example.michellebiol.sampleapp.Models.TokenRequest;
import com.example.michellebiol.sampleapp.Models.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    Button btnUserSignUp;
    EditText userEmail;
    EditText userUsername;
    EditText userPassword;
    EditText confirmPassword;
    IRegisterUserApi services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail = (EditText) findViewById(R.id.userEmail);
        userUsername = (EditText) findViewById(R.id.userUsername);
        userPassword = (EditText) findViewById(R.id.userPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        services = retrofit.create(IRegisterUserApi.class);
    }

    public void register(View v) {
        if (passwordConfirmation(userPassword.getText().toString(), confirmPassword.getText().toString())) {
            final RegisterUserRequest registerUser = new RegisterUserRequest();

            registerUser.setUsername(userUsername.getText().toString());
//            registerUser.setEmail(userEmail.getText().toString());
            registerUser.setPassword(userPassword.getText().toString());


            Call<RegisterUserResponse> registerUserResponseCall = services.newUser(registerUser);
            registerUserResponseCall.enqueue(new Callback<RegisterUserResponse>() {
                @Override
                public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {

                    if (passwordConfirmation(userPassword.getText().toString(), confirmPassword.getText().toString())) {
                        if (response.isSuccessful()) {
                            RegisterUserResponse registerUserResponse = response.body();
                            Toast.makeText(RegisterActivity.this, registerUserResponse.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean passwordConfirmation(String password, String retypePassword) {
        if (password.equals(retypePassword)) {
            return true;
        } else {
            return false;
        }

    }

}
