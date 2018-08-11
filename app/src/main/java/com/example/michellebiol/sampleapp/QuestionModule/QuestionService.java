package com.example.michellebiol.sampleapp.QuestionModule;

import android.content.Context;

import com.example.michellebiol.sampleapp.Interfaces.IQuestionByCategoryApi;
import com.example.michellebiol.sampleapp.Models.QuestionsItem;
import com.example.michellebiol.sampleapp.Models.QuestionsResponse;
import com.example.michellebiol.sampleapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionService {


    public static Retrofit QuestionAPIService(Context context)
    {
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
