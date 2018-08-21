package com.example.michellebiol.sampleapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Interfaces.IGetUserRegisterQuestionApi;
import com.example.michellebiol.sampleapp.Interfaces.ILoginAnswerApi;
import com.example.michellebiol.sampleapp.Interfaces.ILoginUserApi;
import com.example.michellebiol.sampleapp.Models.GetUserRegisterQuestionResponse;
import com.example.michellebiol.sampleapp.Models.LoginAnswer;
import com.example.michellebiol.sampleapp.Models.TokenRequest;
import com.example.michellebiol.sampleapp.Models.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class usernameFragment extends Fragment {


    private EditText userAnswer;
    private TextView userQuestion;
    ILoginAnswerApi iLoginAnswerApi;



    public usernameFragment() {
        // Required empty public constructor
    }

    private void userAnswerRegisterQuestion(final Context context , int user_id , final String username , String user_answer )
    {
       Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iLoginAnswerApi = retrofit.create(ILoginAnswerApi.class);
        Call<LoginAnswer> call = iLoginAnswerApi.getLoginAnswerResult(user_id,user_answer);
        call.enqueue(new Callback<LoginAnswer>() {
            @Override
            public void onResponse(Call<LoginAnswer> call, Response<LoginAnswer> response) {
                if (response.isSuccessful() && Boolean.valueOf(response.body().getResult()))
                {
                    loginRequest(username,context);
                } else {
                    userAnswer.setError("Please check your answer");
                }
            }

            @Override
            public void onFailure(Call<LoginAnswer> call, Throwable t) {

            }
        });

    }

    private void loginRequest(String player_name , final Context context)
    {
        //process of login request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ILoginUserApi services = retrofit.create(ILoginUserApi.class);
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setUsername(player_name);
        tokenRequest.setPassword(player_name);
        Call<TokenResponse> tokenResponseCall = services.loginRequest(tokenRequest);
        tokenResponseCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()){
                    TokenResponse tokenResponse = response.body();
                    String token_type = tokenResponse.getToken_type();
                    String token = tokenResponse.getAccess_token();
                    String user_id = tokenResponse.getId();
                    SharedPreferences sharedPref = context.getSharedPreferences("tokens",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token_type",token_type);
                    editor.putString("token",token);
                    editor.putString("user_id",user_id);
                    editor.apply();
                    Intent intent = new Intent(context , HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) { }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final int user_id = this.getArguments().getInt("user_id");
        final String user_question = this.getArguments().getString("user_question");
        final String username = this.getArguments().getString("username");
        final View v = inflater.inflate(R.layout.fragment_username,container,false);
        userAnswer = (EditText) v.findViewById(R.id.userAnswer);
        userQuestion = (TextView) v.findViewById(R.id.userQuestion);
        userQuestion.setText(user_question);
        Button btnProceed = (Button) v.findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswerRegisterQuestion(
                        container.getContext(),user_id,username.toString(),userAnswer.getText().toString()
                );
            }
        });
        return v;

    }
}
