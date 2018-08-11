package com.example.michellebiol.sampleapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.AnswerModule.Check;
import com.example.michellebiol.sampleapp.Handlers.MyEvents;
import com.example.michellebiol.sampleapp.Handlers.MyHandlers;
import com.example.michellebiol.sampleapp.Helpers.RandomizeHelper;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.Interfaces.IAnswerApi;
import com.example.michellebiol.sampleapp.Interfaces.IQuestionByCategoryApi;
import com.example.michellebiol.sampleapp.LifeModule.Life;
import com.example.michellebiol.sampleapp.Models.AnswerRequest;
import com.example.michellebiol.sampleapp.Models.AnswerResponse;
import com.example.michellebiol.sampleapp.Models.QuestionsItem;
import com.example.michellebiol.sampleapp.Models.QuestionsResponse;
import com.example.michellebiol.sampleapp.PointModule.Points;
import com.example.michellebiol.sampleapp.QuestionModule.Question;
import com.example.michellebiol.sampleapp.TimerModule.CountDown;
import com.example.michellebiol.sampleapp.databinding.ActivityCategoryQuestionBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryQuestion extends AppCompatActivity {

    private List<QuestionsItem> questionsItems;
    private static final long counter = 21000;
    CountDownTimer countDownTimer;
    IQuestionByCategoryApi service;
    IAnswerApi answerService;
    TextView  questionResult , questionFunFacts;

    private LinearLayout displayResult , questionLayout;
    Button btnNext,btnShareOnFB;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Life life;
    Points p;
    boolean isCounterRunning = false;
    CountDown countDown;
    public ActivityCategoryQuestionBinding categoryQuestionBinding;
    Question questionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryQuestionBinding = DataBindingUtil.setContentView(this,R.layout.activity_category_question);
        life = new Life(this);
        p = new Points(this);
        categoryQuestionBinding.setLifemodule(life);
        startTimer(counter);

        questionResult = findViewById(R.id.questionResult);
        questionFunFacts = findViewById(R.id.questionFunFacts);
        displayResult = findViewById(R.id.displayResult);
        questionLayout = findViewById(R.id.questionLayout);
        btnNext = findViewById(R.id.btnNext);
        btnShareOnFB = findViewById(R.id.btnShareOnFB);

        categoryQuestionBinding.setEvent(new MyEvents() {
            @Override
            public void onCustomCheckChanged(RadioGroup radio , int id) {
                 MyHandlers.avoidMultipleClick();
                 RadioButton selected = (RadioButton)findViewById(id);
                 Check.answer = (String) selected.getText();
                 Check.checkAnswer(CategoryQuestion.this);
                 categoryQuestionBinding.setLifemodule(life);
            }
        });

        //initialize retrofit for answer api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        answerService = retrofit.create(IAnswerApi.class);

        //Init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        btnShareOnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnShareOnFB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {
                                Toast.makeText(CategoryQuestion.this, "Successfully share!", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(CategoryQuestion.this, "Share cancel", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Toast.makeText(CategoryQuestion.this, error.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setQuote("We are still working on it.")
                                .setContentUrl(Uri.parse("https://quick-guess.herokuapp.com/"))
                                .setShareHashtag(new ShareHashtag.Builder()
                                        .setHashtag("#Quick-Guess Fun Facts")
                                        .build())
                                .build();

                        if (ShareDialog.canShow(ShareLinkContent.class))
                        {
                            shareDialog.show(linkContent);
                        }
                    }
                });
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.start();
                displayNewQuestion();
            }
        });

        }


    private void getQuestions()
    {

        SharedPreferenceHelper.PREF_FILE = "tokens";
        String token_type = SharedPreferenceHelper.getSharedPreferenceString(this,"token_type",null);
        String token = SharedPreferenceHelper.getSharedPreferenceString(this,"token",null);

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


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        countDownTimer.cancel();
    }


    private String[] getChoices(QuestionsItem choices)
    {
        return new String[]{choices.getChoice_a(), choices.getChoice_b(), choices.getChoice_c(), choices.getChoice_d()};
    }

    private void setQuestion()
    {

        int size = questionsItems.size();

        if (size == 0){
            Toast.makeText(this, "congratulations you answered all the questions", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //randomize all the question and get the first question in the element
            QuestionsItem randQuestion = RandomizeHelper.questions(questionsItems, size).get(0);

            //pass the question list and get all the choices in question
            String[] choices = getChoices(randQuestion);

            //shuffle / randomize all the choices
            String[] shuffledChoices = RandomizeHelper.choices(choices,choices.length);


            //data binding
            questionClass  = new Question(
                    randQuestion.getId(),randQuestion.getQuest(),shuffledChoices[0],
                    shuffledChoices[1],shuffledChoices[2],shuffledChoices[3],randQuestion.getFun_facts()
            );

            //set the collected values
            categoryQuestionBinding.setQuestions(questionClass);

            Check.correctAnswer(randQuestion.getCorrect_answer());
        }

    }


    private void displayResult(String correctOrWrong) {
        questionLayout.setVisibility(View.INVISIBLE);
        questionResult.setText("Result : " + correctOrWrong);
        displayResult.setVisibility(View.VISIBLE);
        getQuestions();
        Toast.makeText(this, "Points : " + String.valueOf(p.getPoints()), Toast.LENGTH_SHORT).show();

    }

    private void displayNewQuestion() {
        questionLayout.setVisibility(View.VISIBLE);
        displayResult.setVisibility(View.INVISIBLE);
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

    private  void startTimer(final long counter) {
       countDown = new CountDown(21000);
       if (!isCounterRunning) {
           isCounterRunning  = true;
           countDownTimer = new CountDownTimer(counter, 1000) {
               @Override
               public void onTick(long timeRemaining) {
                   countDown.setTimer(
                           String.valueOf(TimeUnit.MILLISECONDS.toSeconds(timeRemaining) -
                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining)))
                   );
                   categoryQuestionBinding.setCountdown(countDown);
               }

               @Override
               public void onFinish() {
                   isCounterRunning = false;
                   Check.answer = "No answer";
               }
           };
           countDownTimer.start();
       } else {
           countDownTimer.cancel();
           countDownTimer.start();
       }
   }



}
