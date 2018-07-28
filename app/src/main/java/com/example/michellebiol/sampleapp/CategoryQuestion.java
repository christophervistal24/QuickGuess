package com.example.michellebiol.sampleapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Adapters.QuestionsAdapter;
import com.example.michellebiol.sampleapp.Interfaces.IAnswerApi;
import com.example.michellebiol.sampleapp.Interfaces.IQuestionByCategoryApi;
import com.example.michellebiol.sampleapp.Models.AnswerRequest;
import com.example.michellebiol.sampleapp.Models.AnswerResponse;
import com.example.michellebiol.sampleapp.Models.QuestionsItem;
import com.example.michellebiol.sampleapp.Models.QuestionsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryQuestion extends AppCompatActivity {

    private RecyclerView questionRecyclerView;
    private RecyclerView.Adapter adapter;
    private List<QuestionsItem> questionsItems;
    private static final int counter = 21;
    CountDownTimer countDownTimer;
    IQuestionByCategoryApi service;
    IAnswerApi answerService;
    TextView questionId , question , timerCountDown;
    RadioGroup RGroup;
    RadioButton choice_a ,choice_b , choice_c , choice_d , radioButton;
    private long mLastClickTime = 0;
    private String correctAnswer , funFacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_question);

        questionId = (TextView) findViewById(R.id.questionId);
        question = (TextView) findViewById(R.id.question);
        choice_a = (RadioButton) findViewById(R.id.choice_a);
        choice_b = (RadioButton) findViewById(R.id.choice_b);
        choice_c = (RadioButton) findViewById(R.id.choice_c);
        choice_d = (RadioButton) findViewById(R.id.choice_d);
        RGroup = (RadioGroup) findViewById(R.id.RGroup);
        timerCountDown = (TextView) findViewById(R.id.timerCountDown);

        //initialize retrofit for answer api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        answerService = retrofit.create(IAnswerApi.class);
    }


    private void getQuestions()
    {

        SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token_type = sharedPref.getString("token_type","");
        String token = sharedPref.getString("token","");
        questionsItems = new ArrayList<>();

        Intent i = getIntent();
        String id = i.getStringExtra("category_id");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IQuestionByCategoryApi.class);

        Call<List<QuestionsResponse>> call = service.getQuestions(token_type+token,Integer.parseInt(id));

        call.enqueue(new Callback<List<QuestionsResponse>>() {

            @Override
            public void onResponse(Call<List<QuestionsResponse>> call, Response<List<QuestionsResponse>> response) {
                List<QuestionsResponse> questions = response.body();
                for(QuestionsResponse q: questions)
                {
                    QuestionsItem questionsItem = new QuestionsItem(
                            q.getId(),q.getQuestion(),q.getQuestion_categories_id(),
                            q.getChoice_a(),q.getChoice_b(),q.getChoice_c(),q.getChoice_d(),q.getCorrect_answer(),
                            q.getFun_facts(),q.getQuestion_result());
                    questionsItems.add(questionsItem);
                }

//                adapter = new QuestionsAdapter(questionsItems,getApplicationContext());
//                questionRecyclerView.setAdapter(adapter);
                if(response.isSuccessful())
                {
                    setQuestion();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionsResponse>> call, Throwable t) {
                //add some custom message for exception
            }
        });

    }

    @Override
    protected void onResume()
    {
        getQuestions();
        super.onResume();
    }

    private List<QuestionsItem> generateRandomQuestion(List<QuestionsItem> arr , int n)
    {
        Random r = new Random();

        for(int i = n-1; i > 0; i--)
        {
            int j = r.nextInt(i);

            QuestionsItem temp = arr.get(i);
            arr.set(i, arr.get(j));
            arr.set(j, temp);
        }

        return arr;
    }

    //randomize the choices

    public String[] randomize(String arr[] , int n)
    {
        Random r = new Random();

        for(int i = n-1; i > 0; i--)
        {
            int j = r.nextInt(i);

            String temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        return arr;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    private void setQuestion()
    {
        int size = questionsItems.size();
        if (size == 0){
            Toast.makeText(this, "congratulations you answered all the questions", Toast.LENGTH_SHORT).show();
            return;
        } else {
            QuestionsItem randQuestion = generateRandomQuestion(questionsItems, size).get(0);
            String[] arr = {
                    randQuestion.getChoice_a(),
                    randQuestion.getChoice_b(),
                    randQuestion.getChoice_c(),
                    randQuestion.getChoice_d()
            };
            int n = arr.length;
            String[] shuffledChoices = randomize(arr,n);

            questionId.setText(randQuestion.getId());
            question.setText(randQuestion.getQuest());
            correctAnswer = randQuestion.getCorrect_answer();
            funFacts = randQuestion.getFun_facts();
            choice_a.setText(shuffledChoices[0]);
            choice_b.setText(shuffledChoices[1]);
            choice_c.setText(shuffledChoices[2]);
            choice_d.setText(shuffledChoices[3]);
        }

    }





    public void getSelected(View v)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        radioButton = findViewById(R.id.choice_d);
        isCorrect(radioButton.getText().toString());
        getQuestions();
        RGroup.clearCheck();
    }

    //send the user status to the database using retrofit
    private void sendAnswer(String questionId, String result)
    {
        //get the user token
        SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token_type = sharedPref.getString("token_type","");
        String token = sharedPref.getString("token","");

        final AnswerRequest answerRequest = new AnswerRequest();
        answerRequest.setQuestion_id(Integer.parseInt(questionId));
        answerRequest.setQuestion_result(result);

        Call<AnswerResponse> answerResponseCall = answerService.userAnswer(token_type.concat(token),answerRequest);
        answerResponseCall.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {

            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                Toast.makeText(CategoryQuestion.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //check if correct or not
    private String isCorrect(String selectedAnswer)
    {

        String result = null;
        if(selectedAnswer.equals(correctAnswer))
        {
            result = "Correct";
        }
        else
        {
            result = "Wrong";
        }
            sendAnswer(questionId.getText().toString(),"correct");
            return result;
    }







}