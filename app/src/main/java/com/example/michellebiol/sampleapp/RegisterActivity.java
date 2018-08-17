package com.example.michellebiol.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Interfaces.IRegisterQuestionApi;
import com.example.michellebiol.sampleapp.Models.RegisterQuestionResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    IRegisterQuestionApi services;
    ArrayList<String> questionList;
    private EditText userUsername;
    private EditText userQuestionAnswer;
    private Spinner spinnerQuestion;
    private int checkSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getRegisterQuestions();
        spinnerQuestion = (Spinner) findViewById(R.id.spinnerQuestions);
        userUsername = (EditText) findViewById(R.id.userUsername);
        userQuestionAnswer = (EditText) findViewById(R.id.userQuestionAnswer);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuestion.setAdapter(adapter);
    }


    private void getRegisterQuestions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit.create(IRegisterQuestionApi.class);

        questionList = new ArrayList<>();
        questionList.add("Please choose a question…");

        Call<List<RegisterQuestionResponse>> registerQuestionResponseCall = services.getQuestions();
        registerQuestionResponseCall.enqueue(new Callback<List<RegisterQuestionResponse>>() {
            @Override
            public void onResponse(Call<List<RegisterQuestionResponse>> call, Response<List<RegisterQuestionResponse>> response) {
                List<RegisterQuestionResponse> questions = response.body();

                if (questions != null && response.isSuccessful()) {
                    for (RegisterQuestionResponse q : questions) {
                        questionList.add(q.getQuestion());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RegisterQuestionResponse>> call, Throwable t) {

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

    private boolean isSelectQuestionEmpty(Spinner selectQuestion)
    {
        return selectQuestion.getSelectedItem().toString().equalsIgnoreCase("Please choose a question…");
    }

    public void retrieveAllAcounts(View v)
    {
        checkSum = 0;
        if  (isUsernameEmpty(userUsername))
        {
            userUsername.setError("Please provide some username");
        } else {
            checkSum+=1;
        }
        if (isSelectQuestionEmpty(spinnerQuestion))
        {
            ((TextView)spinnerQuestion.getSelectedView()).setError("Please select question");
        } else {
            checkSum+=1;
        }
        if  (isAnswerEmpty(userQuestionAnswer))
        {
            userQuestionAnswer.setError("Please provide some answer");
        } else {
            checkSum +=1;
        }

        isPassed(checkSum);
    }

    private void isPassed(int checkSum) {
        if (checkSum >= 3) {
            Toast.makeText(this, "Passed!", Toast.LENGTH_SHORT).show();
        } else {
            return;
        }
    }


}
