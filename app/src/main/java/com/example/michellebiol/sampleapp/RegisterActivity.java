package com.example.michellebiol.sampleapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Handlers.MyHandlers;
import com.example.michellebiol.sampleapp.Interfaces.IGetUserRegisterQuestionApi;
import com.example.michellebiol.sampleapp.Interfaces.IUsernameApi;
import com.example.michellebiol.sampleapp.Models.GetUserRegisterQuestionResponse;
import com.example.michellebiol.sampleapp.Models.UsernameResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterActivity extends AppCompatActivity {

    private EditText userUsername;
    IUsernameApi iUsernameApi;
    IGetUserRegisterQuestionApi iGetUserRegisterQuestionApi;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userUsername = (EditText) findViewById(R.id.userUsername);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iUsernameApi = retrofit.create(IUsernameApi.class);
        bundle = new Bundle();
    }


    private void getUserRegisterQuestion(int user_id, final FragmentTransaction fragmentTransaction, final usernameFragment f1)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iGetUserRegisterQuestionApi = retrofit.create(IGetUserRegisterQuestionApi.class);
        Call<GetUserRegisterQuestionResponse> call = iGetUserRegisterQuestionApi.getRegisterQuestion(user_id);
        call.enqueue(new Callback<GetUserRegisterQuestionResponse>() {
            @Override
            public void onResponse(Call<GetUserRegisterQuestionResponse> call, Response<GetUserRegisterQuestionResponse> response) {
                bundle.putString("user_question",response.body().getQuestion());
                f1.setArguments(bundle);
                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(Call<GetUserRegisterQuestionResponse> call, Throwable t) {

            }
        });
    }

    private boolean isUsernameEmpty(EditText username)
    {
        return username.getText().toString().trim().length() <= 0;
    }

    private boolean isAnswerEmpty(EditText answer)
    {
        return answer.getText().toString().trim().length() <= 0;
    }

    public void retrieveAllAcounts(View v)
    {
        Call<UsernameResponse> call = iUsernameApi.getCheckResult(userUsername.getText().toString());
        if (isUsernameEmpty(userUsername))
        {
            userUsername.setError("Please provide valid username / playername");
        } else {
            call.enqueue(new Callback<UsernameResponse>() {
                @Override
                public void onResponse(Call<UsernameResponse> call, Response<UsernameResponse> response) {
                    if  (response.isSuccessful())
                    {
                        if (response.body() != null) {
                            if (Boolean.valueOf(response.body().getFound())){

                                bundle.putInt("user_id",response.body().getId());
                                bundle.putString("username",userUsername.getText().toString());

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                usernameFragment f1 = new usernameFragment();
                                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.add(R.id.fragment_one,f1);
                                getUserRegisterQuestion(response.body().getId(),fragmentTransaction , f1);
                            } else {
                                userUsername.setError("Invalid username / playername");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UsernameResponse> call, Throwable t) {

                }
            });


        }
    }

}
